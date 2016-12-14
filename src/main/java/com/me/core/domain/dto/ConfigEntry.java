package com.me.core.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.me.core.domain.entities.Blacklist;
import com.me.core.service.utils.ItemDeserializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonDeserialize(using = ItemDeserializer.class)
public class ConfigEntry {
    @NonNull
    private Blacklist blacklist;
    @NonNull
    private String folderName;
    @NonNull
    private boolean useProxy;

    public ConfigEntry(String blacklistName, String folderName, String website, boolean useProxy) {
        this.blacklist = new Blacklist(blacklistName, website, new Date());
        this.folderName = folderName;
        this.useProxy = useProxy;
    }
}
