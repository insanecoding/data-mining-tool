package com.me.core.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    private Utils() {
    }

    public static void createFilePath(String filePath) {
        File file = new File(filePath);

        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            logger.info("could not create file {}", filePath);
            System.exit(-1);
        }
    }
}
