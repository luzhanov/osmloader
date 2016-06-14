package com.waypointer.osmloader.parser;

import com.waypointer.osmloader.mapping.MappingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author Igor Luzhanov, 03/02/2015
 */
public abstract class TestFullConsumer implements Runnable {

    private Logger logger = LoggerFactory.getLogger(TestFullConsumer.class);

    private final BlockingQueue<MappingResult> mappingsQueue;

    public TestFullConsumer(BlockingQueue<MappingResult> mappingsQueue) {
        this.mappingsQueue = mappingsQueue;
    }

    @Override
    public void run() {
        try {
            List<MappingResult> saveList = new LinkedList<>();
            while (true) {
                MappingResult mappingResult = mappingsQueue.take();

                if (mappingResult == OsmPbfParser.STOP_RESULT) {
                    onGetAllData(saveList);
                    break;
                }
                saveList.add(mappingResult);
            }
        } catch (InterruptedException e) {
            logger.error("Exception during consumer test", e);
        }
    }

    public abstract void onGetAllData(List<MappingResult> resultList);
}
