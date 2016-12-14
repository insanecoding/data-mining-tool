package com.me.core.service.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.core.domain.dto.ConfigEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

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

    public static List<ConfigEntry> parseJson(String jsonConfigPath) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(jsonConfigPath)));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(contents, new TypeReference<Collection<ConfigEntry>>() {});
    }
}
