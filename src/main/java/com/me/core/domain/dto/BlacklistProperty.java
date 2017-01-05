package com.me.core.domain.dto;

import com.me.core.domain.entities.Blacklist;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@NoArgsConstructor
public class BlacklistProperty {
    @NonNull
    private Blacklist blacklist;
    @NonNull
    private String pathName;

    public BlacklistProperty(String blacklistName, String folderName, String website) {
        this.blacklist = new Blacklist(blacklistName, website, new Date());
        this.pathName = folderName;
    }
}
