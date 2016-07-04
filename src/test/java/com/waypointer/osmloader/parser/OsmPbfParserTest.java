package com.waypointer.osmloader.parser;

import com.waypointer.osmloader.TestFullMapping;
import com.waypointer.osmloader.TestingDataUtils;
import com.waypointer.osmloader.mapping.MappingResult;
import com.waypointer.osmloader.mapping.MappingSet;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;

public class OsmPbfParserTest {

    @Test
    public void shouldParseSpringsFromFile() throws Exception {
        MappingSet mappingSet = TestingDataUtils.createSpringPondsMappingSet();

        BlockingQueue<MappingResult> queue = new LinkedBlockingQueue<>();
        OsmPbfParser parser = new OsmPbfParser(queue, getFileFromClasspath("/andorra-latest.osm.pbf"), mappingSet);

        TestFullConsumer consumer = new TestFullConsumer(queue) {
            @Override
            public void onGetAllData(List<MappingResult> resultList) {
                assertThat(resultList).hasSize(12);
            }
        };

        new Thread(parser).start();
        consumer.run(); //do in the current thread
    }

    @Test
    public void shouldParseAllFromFile() {
        MappingSet mappingSet = TestFullMapping.createFullMapping();

        BlockingQueue<MappingResult> queue = new LinkedBlockingQueue<>();
        OsmPbfParser parser = new OsmPbfParser(queue, getFileFromClasspath("/andorra-latest.osm.pbf"), mappingSet);

        TestFullConsumer consumer = new TestFullConsumer(queue) {
            @Override
            public void onGetAllData(List<MappingResult> resultList) {
                assertThat(resultList).hasSize(49);
            }
        };

        new Thread(parser).start();
        consumer.run(); //do in the current thread
    }

    @Test
    public void shouldParseSkippingNodes() {
        MappingSet mappingSet = TestFullMapping.createFullMapping();

        BlockingQueue<MappingResult> queue = new LinkedBlockingQueue<>();
        OsmPbfParser parser = new OsmPbfParser(queue, getFileFromClasspath("/andorra-latest.osm.pbf"), mappingSet);
        parser.setSkipFirstNodes(true);

        TestFullConsumer consumer = new TestFullConsumer(queue) {
            @Override
            public void onGetAllData(List<MappingResult> resultList) {
                assertThat(resultList).hasSize(2);
            }
        };

        new Thread(parser).start();
        consumer.run(); //do in the current thread
    }

    private File getFileFromClasspath(String filename) {
        String path =  getClass().getResource(filename).getFile();
        return new File(path);
    }

}