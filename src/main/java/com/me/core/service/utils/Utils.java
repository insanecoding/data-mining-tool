package com.me.core.service.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public final class Utils {

    private Utils() {
    }

    public static void createFilePath(String filePath) {
        File file = new File(filePath);

        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            log.error("could not create file {}", filePath);
            System.exit(-1);
        }
    }
}
