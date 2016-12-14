package com.me.core.service.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.me.core.domain.dto.ConfigEntry;

import java.io.IOException;

public class ItemDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String blacklistName = node.get("blacklistName").asText();
        String folderName = node.get("folderName").asText();
        String website = node.get("website").asText();
        boolean useProxy = node.get("useProxy").asBoolean();

        return new ConfigEntry(blacklistName, folderName, website, useProxy);
    }
}
