package com.me.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MyExceptionHandler {

    private static Logger logger =
            LoggerFactory.getLogger(MyExceptionHandler.class);

    private MyExceptionHandler() {
    }

    public static void handleExceptions(Exception e) {
        if (e.getClass().getName().equals(InterruptedException.class.getName())) {
            logger.info("async execution interrupted");
        } else {
            logger.error("exception occurred! {} - {}", e.getClass(), e.getMessage());
            logger.error("Stacktrace: ");
            e.printStackTrace();
            logger.error("{}", (Object[]) e.getStackTrace());
            System.exit(-1);
        }
    }
}
