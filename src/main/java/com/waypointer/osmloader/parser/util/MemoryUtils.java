package com.waypointer.osmloader.parser.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public final class MemoryUtils {

    public static final int MB = 1024 * 1024;

    private static Logger logger = LoggerFactory.getLogger(MemoryUtils.class);

    private MemoryUtils() {
    }

    public static void logUsedMemory(String classifier) {
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();

        long usedMemory = total - free;

        String memMb = (usedMemory / MB) + "";

        logger.debug("Used memory [{}] is [{} Mb]", classifier, memMb);
    }

    public static String getFormattedTimeFromDate(Long date) {
        long millis = getMillisecondsFromDate(date);

        return String.format("%d min:%d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    private static long getMillisecondsFromDate(Long date) {
        return System.currentTimeMillis() - date;
    }
}
