package com.waypointer.osmloader.parser;

import com.waypointer.osmloader.mapping.MappingResult;
import com.waypointer.osmloader.mapping.MappingRule;
import com.waypointer.osmloader.mapping.MappingSet;
import com.waypointer.osmloader.parser.util.MemoryUtils;
import com.waypointer.osmloader.parser.util.SincHelper;
import com.waypointer.osmloader.utils.Coordinate;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.set.hash.TLongHashSet;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.pbf2.v0_6.PbfReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * @author Igor Luzhanov, 08/01/2015
 */
public class OsmPbfParser implements Runnable {

    public final static MappingResult STOP_RESULT = new MappingResult(new Node(-1L, 0, new Date(), null,0, 0.0, 0.0), (MappingRule) null);

    private static Logger logger = LoggerFactory.getLogger(OsmPbfParser.class);

    private final TLongHashSet wayNodesIds = new TLongHashSet();
    private final TLongObjectHashMap<Coordinate> coordinateMap = new TLongObjectHashMap<>();

    private final BlockingQueue<MappingResult> mappingsQueue;

    private PbfReader reader;

    int nodesAdded = 0;

    private final File file;
    private final MappingSet mappingSet;

    private boolean skipFirstNodes = false;

    public OsmPbfParser(BlockingQueue<MappingResult> mappingsQueue, File file, MappingSet mapping) {
        this.mappingsQueue = mappingsQueue;
        this.file = file;
        this.mappingSet = mapping;
    }

    public void setSkipFirstNodes(boolean skipFirstNodes) {
        this.skipFirstNodes = skipFirstNodes;
        logger.debug("Skipping first node pass, only ways will be processed");
    }

    @Override
    public void run() {
        parse(file);
    }

    private void parse(File file) {
        try {
            read(file);
        } catch (Exception e) {
            logger.error("Error during file reading", e);
        }
    }

    private void read(File pbf) throws Exception {
        reader = new PbfReader(pbf, 1);
        MemoryUtils.logUsedMemory("Before file parsing");

        reader.setSink(new FirstLoadingSync());
        reader.run();
    }

    private void logProgress(int count) {
        int mod = 1000000;
        if ((count) % mod == 0) {
            logger.debug("Parsed [{} M] objects", (count) / mod);
        }
    }

    private class FirstLoadingSync extends SincHelper {

        int totalNodesCnt = 0;
        int totalWayCnt = 0;
        int interestingWayCnt = 0;


        @Override
        public void process(EntityContainer container) {
            Entity entity = container.getEntity();
            Collection<MappingRule> mappingRules;

            switch (entity.getType()) {
                case Node:
                    if (skipFirstNodes) {
                        break;
                    }

                    totalNodesCnt++;
                    logProgress(totalNodesCnt + totalWayCnt);

                    mappingRules = mappingSet.getMappingRulesForEntity(entity);
                    if (mappingRules != null && !mappingRules.isEmpty()) {
                        mappingsQueue.add(new MappingResult(entity, mappingRules));
                        nodesAdded++;
                    }

                    break;
                case Way:
                    totalWayCnt++;
                    logProgress(totalNodesCnt + totalWayCnt);

                    mappingRules = mappingSet.getMappingRulesForEntity(entity);
                    if (mappingRules != null && !mappingRules.isEmpty()) {
                        interestingWayCnt++;
                        Way way = (Way) entity;
                        for (WayNode node : way.getWayNodes()) {
                            wayNodesIds.add(node.getNodeId());
                        }
                    }
                    break;
                default:
                    //do nothing
            }
        }

        @Override
        public void complete() {
            System.gc();
            logger.debug("Total nodes count [{}]", totalNodesCnt);
            logger.debug("Total ways count [{}]", totalWayCnt);
            logger.debug("Nodes added count [{}]", nodesAdded);

            MemoryUtils.logUsedMemory("After nodes parsing (step 1)");
            reader.setSink(new CoordinatesPassSync());
            reader.run();
        }
    }

    private class CoordinatesPassSync extends SincHelper {
        private int nodesProcessed = 0;

        @Override
        public void process(EntityContainer container) {
            Entity entity = container.getEntity();
            switch (entity.getType()) {
                case Node:
                    Node node = (Node) entity;

                    if (wayNodesIds.contains(node.getId())) {
                        coordinateMap.put(node.getId(), new Coordinate(node.getLatitude(), node.getLongitude()));
                        wayNodesIds.remove(node.getId());
                        nodesProcessed++;

                        if (nodesProcessed % 1000000 == 0) {
                            System.gc();
                            int millionNodesCnt = nodesProcessed / 1000000;
                            MemoryUtils.logUsedMemory("Processed coordinates nodes [" + millionNodesCnt + "M]");
                        }
                    }
                    break;
                default:
                    //do nothing
            }
        }

        @Override
        public void complete() {
            System.gc();
            logger.debug("Coordinates processed on second step [{}]", nodesProcessed);
            MemoryUtils.logUsedMemory("After coordinates parsing (step 2)");
            reader.setSink(new WayNodesExtractSync());
            reader.run();
        }
    }

    private class WayNodesExtractSync extends SincHelper {
        int interestingWayCnt = 0;

        @Override
        public void process(EntityContainer container) {
            Entity entity = container.getEntity();
            Collection<MappingRule> mappingRules;

            int totalWayCnt = 0;

            switch (entity.getType()) {
                case Way:
                    totalWayCnt++;
                    mappingRules = mappingSet.getMappingRulesForEntity(entity);
                    if (mappingRules != null && !mappingRules.isEmpty()) {
                        interestingWayCnt++;
                        MappingResult mappingResult = new MappingResult(entity, mappingRules);
                        addCoordinatesForWay(mappingResult);
                        mappingsQueue.add(mappingResult);
                    }
                    logProgress(totalWayCnt);
                    break;
                default:
                    //do nothing
            }
        }

        private void addCoordinatesForWay(MappingResult mappingResult) {
            if (mappingResult.getOrigin() instanceof MappingResult.WayOrigin) {
                MappingResult.WayOrigin wayOrigin = (MappingResult.WayOrigin) mappingResult.getOrigin();
                for (WayNode wayNode : wayOrigin.getWayNodes()) {
                    long nodeId = wayNode.getNodeId();
                    wayOrigin.getWayCoordinates().add(coordinateMap.get(nodeId));
                }
            } else {
                logger.error("Wrong type of mappingResult");
            }
        }

        @Override
        public void complete() {
            try {
                mappingsQueue.put(STOP_RESULT);
            } catch (InterruptedException e) {
                logger.error("Error during stopping queue");
            }
            System.gc();
            logger.debug("Way nodes added on third step [{}]", interestingWayCnt);
            MemoryUtils.logUsedMemory("After ways parsing (step 3)");
            logger.debug("Result entities size (nodes & ways with tags) [{}]", nodesAdded + interestingWayCnt);
        }
    }

}
