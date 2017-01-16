package com.me.common.initializer;

import com.me.common.MyExecutable;
import com.me.core.domain.dto.BlacklistProperty;
import com.me.core.service.importbl.BlacklistImporterService;
import com.me.core.service.uncompress.UncompressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UncompressImportInitializer implements Initializer {

    private final UncompressService uncompressService;
    private final BlacklistImporterService importerService;
    private final DownloadInitializer next;

    @Autowired
    public UncompressImportInitializer(UncompressService uncompressService,
                                       BlacklistImporterService importerService, DownloadInitializer next) {
        this.uncompressService = uncompressService;
        this.importerService = importerService;
        this.next = next;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Map<String, Object> dto, List<MyExecutable> executables) {

        Map<String, Object> settings = (Map<String, Object>) dto.get("import");
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

            uncompressService.setCompressed(new ArrayList<>(compressed));
            importerService.setBlacklistProperties(new ArrayList<>(importer));
            executables.add(uncompressService);
            executables.add(importerService);


        }
        next.initialize(dto, executables);
    }
}
