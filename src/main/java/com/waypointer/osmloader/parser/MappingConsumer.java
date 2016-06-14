package com.waypointer.osmloader.parser;

import com.waypointer.osmloader.mapping.MappingResult;
import com.waypointer.osmloader.parser.util.MemoryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * @author Igor Luzhanov, 03/02/2015
 */
public class MappingConsumer implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(MappingConsumer.class);

    public static final int BATCH_COUNT = 1000;

    private final BlockingQueue<MappingResult> mappingsQueue;

    private int resultsCnt;

    private final TreeMap<String, Integer> resultCountMap = new TreeMap<>();

    private final Consumer<List<MappingResult>> dbSavingFunction;

    public MappingConsumer(BlockingQueue<MappingResult> mappingsQueue, Consumer<List<MappingResult>> dbSavingFunction) {
        this.mappingsQueue = mappingsQueue;
        this.dbSavingFunction = dbSavingFunction;
    }

    @Override
    public void run() {
        long startDate = System.currentTimeMillis();
        try {
            List<MappingResult> saveList = new LinkedList<>();
            while (true) {
                MappingResult mappingResult = mappingsQueue.take();

                if (mappingResult == OsmPbfParser.STOP_RESULT) {
                    dbSavingFunction.accept(saveList);

                    printStatistics();
                    String spent = MemoryUtils.getFormattedTimeFromDate(startDate);
                    logger.debug("Stopping consumer, totally [{}] results saved, time spent [{}]", resultsCnt, spent);
                    break;
                }
                saveList.add(mappingResult);
                updateStatistics(mappingResult);
                resultsCnt++;

                //save by portions of 100
                if (saveList.size() % BATCH_COUNT == 0) {
                    dbSavingFunction.accept(saveList);
                    saveList = new LinkedList<>();
                }
                if (resultsCnt % 10000 == 0) {
                    MemoryUtils.logUsedMemory("Saving [" + resultsCnt + "] points to DB");
                }
            }
        } catch (InterruptedException e) {
            logger.error("Exception during consuming", e);
        }
    }

    private void printStatistics() {
        String str = resultCountMap.toString().replace(", ", "\n");
        logger.debug(str);
    }

    private void updateStatistics(MappingResult mappingResult) {
        for (String category : mappingResult.getCategoriesCodeStrings()) {
            Integer count = resultCountMap.get(category);
            if (count != null) {
                resultCountMap.put(category, ++count);
            } else {
                resultCountMap.put(category, 1);
            }
        }
    }
}
