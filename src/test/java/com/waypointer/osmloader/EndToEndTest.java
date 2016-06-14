package com.waypointer.osmloader;

import com.waypointer.osmloader.mapping.MappingResult;
import com.waypointer.osmloader.mapping.MappingSet;
import com.waypointer.osmloader.parser.MappingConsumer;
import com.waypointer.osmloader.parser.OsmPbfParser;
import com.waypointer.osmloader.parser.TestFullConsumer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class EndToEndTest {

    private Logger logger = LoggerFactory.getLogger(EndToEndTest.class);

    @Test
    public void parseOnly() throws Exception {
        MappingSet mappingSet = TestFullMapping.createFullMapping();

        BlockingQueue<MappingResult> queue = new LinkedBlockingQueue<>();
        OsmPbfParser parser = new OsmPbfParser(queue, getFileFromClasspath("/montenegro-latest.osm.pbf"), mappingSet);

        TestFullConsumer consumer = new TestFullConsumer(queue) {
            @Override
            public void onGetAllData(List<MappingResult> resultList) {
                assertThat(resultList).hasSize(2802);
            }
        };

        new Thread(parser).start();
        consumer.run(); //do in the current thread
    }

    @Test
    public void parseAndUseConsumer() throws Exception {
        MappingSet mappingSet = TestFullMapping.createFullMapping();

        BlockingQueue<MappingResult> queue = new LinkedBlockingQueue<>();
        OsmPbfParser parser = new OsmPbfParser(queue, getFileFromClasspath("/montenegro-latest.osm.pbf"), mappingSet);

        MappingConsumer consumer = new MappingConsumer(queue,
                mappingResList -> logger.debug("List for DB saving size:" + mappingResList.size()));

        new Thread(parser).start();
        consumer.run(); //do in the current thread for testing
    }

    private File getFileFromClasspath(String filename) {
        String path =  getClass().getResource(filename).getFile();
        return new File(path);
    }

}
