package com.me.common;

import com.me.core.domain.dto.BlacklistProperty;
import com.me.core.service.download.DownloaderService;
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
    @Lazy
    private final DownloaderService downloaderService;

    @Autowired
    public ExecutableInitializer(BlacklistImporterService importerService,
                                 UncompressService uncompressService,
                                 DownloaderService downloaderService) {
        this.importerService = importerService;
        this.uncompressService = uncompressService;
        this.downloaderService = downloaderService;
    }

    public List<MyExecutable> createExecutables(Map<String, Map<String, Object>> dto) {
        Map<String, Object> params = initAll(dto);
        List<MyExecutable> executables = new LinkedList<>();

        if (params.keySet().contains("uncompress")) {
            uncompressService.initialize(params);
            executables.add(uncompressService);
        }
        if (params.keySet().contains("importer")) {
            importerService.initialize(params);
            executables.add(importerService);
        }
        if (params.keySet().contains("importer")) {
            downloaderService.initialize(params);
            executables.add(downloaderService);
        }

        return executables;
    }

    private Map<String, Object> initAll(Map<String, Map<String, Object>> dto) {
        Map<String, Object> params = new LinkedHashMap<>();

        initUncompressImport(dto, params);
        initDownloader(dto, params);

        return params;
    }

    @SuppressWarnings(value = "unchecked")
    private void initUncompressImport(Map<String, Map<String, Object>> dto,
                                      Map<String, Object> params) {
        Map<String, Object> settings = dto.get("import");
        String cwd = (String) settings.get("cwd");

        if ((boolean) settings.get("isOn")) {
            List<Map<String, String>> blacklists =
                    (List<Map<String, String>>) settings.get("blacklists");

            List<String> compressed = blacklists.stream()
                    .map(k -> k.get("folderName"))
                    // concatenate to make absolute path from relative
                    .map(relativePath -> cwd + "\\" + relativePath)
                    .collect(Collectors.toList());

            List<BlacklistProperty> importer = blacklists.stream()
                    .map(k -> {
                        String fullPath = cwd + "\\" + k.get("folderName");
                        String listName = k.get("listName");
                        String website = k.get("website");
                        return new BlacklistProperty(listName, fullPath, website);
                    }).collect(Collectors.toList());

            params.put("uncompress", compressed);
            params.put("importer", importer);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void initDownloader(Map<String, Map<String, Object>> dto,
                                Map<String, Object> params) {
        Map<String, Object> settings = dto.get("download");
        if ((boolean) settings.get("isOn")) {
            Map<String, Object> properties = new LinkedHashMap<>();

            List<Map<String, Object>> categoriesTemp =
                    (List<Map<String, Object>>) settings.get("categories");
            List<String> categories = categoriesTemp.stream()
                    .map(elem -> (String)elem.get("categoryName")).collect(Collectors.toList());

            properties.put("downloadsPerCategory", settings.get("downloadsPerCategory"));
            properties.put("threadsNumber", settings.get("threadsNumber"));
            properties.put("readTimeout", settings.get("readTimeout"));
            properties.put("connectTimeout", settings.get("connectTimeout"));
            properties.put("categories", categories);

            params.put("downloader", properties);
        }
    }
}
