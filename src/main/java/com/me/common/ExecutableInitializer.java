package com.me.common;

import com.me.core.domain.dto.BlacklistProperty;
import com.me.core.service.importbl.BlacklistImporterService;
import com.me.core.service.uncompress.UncompressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExecutableInitializer {

    @Lazy
    private final BlacklistImporterService importerService;
    @Lazy
    private final UncompressService uncompressService;

    @Autowired
    public ExecutableInitializer(BlacklistImporterService importerService, UncompressService uncompressService) {
        this.importerService = importerService;
        this.uncompressService = uncompressService;
    }


    @SuppressWarnings(value = "unchecked")
    public Map<String, Object> initialize(Map<String, Map<String, Object>> clientResponse) {
        Map<String, Object> initSettings = new LinkedHashMap<>();
        Map<String, Object> settings =  clientResponse.get("import");
        final String cwd = (String) settings.get("cwd");

        if ((boolean)settings.get("isOn")) {
            List<LinkedHashMap<String, String>> blacklists =
                    (List<LinkedHashMap<String, String>>) settings.get("blacklists");

            initSettings.put("uncompress", initUncompress(cwd, blacklists));
            initSettings.put("importer", initImporter(cwd, blacklists));
        }
        return initSettings;
    }

    private List<BlacklistProperty> initImporter(String cwd,
                                                 List<LinkedHashMap<String, String>> blacklists) {
        return blacklists.stream()
                .map( k -> {
                    String fullPath = cwd + "\\" + k.get("folderName");
                    String listName = k.get("listName");
                    String website = k.get("website");
                    return new BlacklistProperty(listName, fullPath, website);
                }).collect(Collectors.toList());
    }

    private List<String> initUncompress(String cwd, List<LinkedHashMap<String, String>> blacklists) {
        return blacklists.stream()
                .map( k -> k.get("folderName"))
                // concatenate to make absolute path from relative
                .map( relativePath -> cwd + "\\" + relativePath)
                .collect(Collectors.toList());
    }


    public List<MyExecutable> createExecutables(Map<String, Object> params) {
        List<MyExecutable> executables = new LinkedList<>();
        if (params.keySet().contains("uncompress")) {
            uncompressService.initialize(params);
            executables.add(uncompressService);
        }
        if (params.keySet().contains("importer")) {
            importerService.initialize(params);
            executables.add(importerService);
        }

        return executables;
    }
}
