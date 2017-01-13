package com.me.common;

import com.me.core.domain.dto.BlacklistProperty;
import com.me.core.service.download.DownloaderService;
import com.me.core.service.features.nGrams.NGramExtractorService;
import com.me.core.service.features.tag.TagStatExtractService;
import com.me.core.service.features.text.TextFromTagExtractorService;
import com.me.core.service.features.text.TextMainExtractorService;
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
    @Lazy
    private final TextMainExtractorService textMainExtractorService;
    @Lazy
    private final TextFromTagExtractorService textFromTagExtractorService;
    @Lazy
    private final NGramExtractorService nGramExtractorService;
    @Lazy
    private final TagStatExtractService tagStatExtractService;

    @Autowired
    public ExecutableInitializer(BlacklistImporterService importerService,
                                 UncompressService uncompressService,
                                 DownloaderService downloaderService,
                                 TextMainExtractorService textMainExtractorService,
                                 TextFromTagExtractorService textFromTagExtractorService,
                                 NGramExtractorService nGramExtractorService,
                                 TagStatExtractService tagStatExtractService) {
        this.importerService = importerService;
        this.uncompressService = uncompressService;
        this.downloaderService = downloaderService;
        this.textMainExtractorService = textMainExtractorService;
        this.textFromTagExtractorService = textFromTagExtractorService;
        this.nGramExtractorService = nGramExtractorService;
        this.tagStatExtractService = tagStatExtractService;
    }

    public List<MyExecutable> createExecutables(Map<String, Object> dto) {
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
        if (params.keySet().contains("downloader")) {
            downloaderService.initialize(params);
            executables.add(downloaderService);
        }
        if (params.keySet().contains("textMainExtractor")) {
            textMainExtractorService.initialize(params);
            executables.add(textMainExtractorService);
        }
        if (params.keySet().contains("textFromTagExtractor")) {
            textFromTagExtractorService.initialize(params);
            executables.add(textFromTagExtractorService);
        }
        if (params.keySet().contains("nGramsExtractor")) {
            nGramExtractorService.initialize(params);
            executables.add(nGramExtractorService);
        }
        if (params.keySet().contains("tagStatExtractor")) {
            tagStatExtractService.initialize(params);
            executables.add(tagStatExtractService);
        }

        return executables;
    }

    private Map<String, Object> initAll(Map<String, Object> dto) {
        Map<String, Object> params = new LinkedHashMap<>();

        initUncompressImport(dto, params);
        initDownloader(dto, params);
        initExtractors(dto, params);

        return params;
    }

    @SuppressWarnings(value = "unchecked")
    private void initUncompressImport(Map<String, Object> dto,
                                      Map<String, Object> params) {
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

            params.put("uncompress", compressed);
            params.put("importer", importer);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void initDownloader(Map<String, Object> dto,
                                Map<String, Object> params) {
        Map<String, Object> settings = (Map<String, Object>) dto.get("download");
        if ((boolean) settings.get("isOn")) {
            Map<String, Object> properties = new LinkedHashMap<>();

            List<Map<String, Object>> categoriesTemp =
                    (List<Map<String, Object>>) settings.get("categories");
            List<String> categories = createListFromMap(categoriesTemp);

            properties.put("downloadsPerCategory", settings.get("downloadsPerCategory"));
            properties.put("threadsNumber", settings.get("threadsNumber"));
            properties.put("readTimeout", settings.get("readTimeout"));
            properties.put("connectTimeout", settings.get("connectTimeout"));
            properties.put("categories", categories);

            params.put("downloader", properties);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void initExtractors(Map<String, Object> dto, Map<String, Object> params) {
        Map<String, Object> settings = (Map<String, Object>) dto.get("extract");
        if ((boolean) settings.get("isOn")) {
            List<Map<String, Object>> categories =
                    (List<Map<String, Object>>) settings.get("categories");
            List<String> stringCategories = createListFromMap(categories);

            initTextMain(params, settings, stringCategories);
            initTextFromTags(params, settings, stringCategories);
            initNGrams(params, settings, stringCategories);
            initTagStatExtractor(params, settings, stringCategories);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void initTagStatExtractor(Map<String, Object> params,
                                      Map<String, Object> settings,
                                      List<String> stringCategories) {
        boolean isTagStat = (boolean) settings.get("isTagStat");
        List<Map<String, Object>> tagsToSkip = (List<Map<String, Object>>) settings.get("tagsToSkip");
        List<String> tagsToSkipStr = createListFromMap(tagsToSkip);
        Map<String, Object> init = new LinkedHashMap<>();
        init.put("categories", stringCategories);
        init.put("tagsToSkip", tagsToSkipStr);
        if (isTagStat)
            params.put("tagStatExtractor", init);
    }

    private void initTextMain(Map<String, Object> params,
                              Map<String, Object> settings, List<String> stringCategories) {
        boolean isTextMain = (boolean) settings.get("isTextMain");
        if (isTextMain)
            params.put("textMainExtractor", stringCategories);
    }

    private void initNGrams(Map<String, Object> params,
                            Map<String, Object> settings, List<String> stringCategories) {
        boolean isNGrams = (boolean) settings.get("isNgrams");
        int maxNGramSize = (int) settings.get("maxNGramSize");
        Map<String, Object> nGramExtractor = new LinkedHashMap<>();
        nGramExtractor.put("categories", stringCategories);
        nGramExtractor.put("maxNGramSize", maxNGramSize);
        if (isNGrams)
            params.put("nGramsExtractor", nGramExtractor);
    }

    @SuppressWarnings(value = "unchecked")
    private void initTextFromTags(Map<String, Object> params,
                                  Map<String, Object> settings, List<String> stringCategories) {
        boolean isTextFromTags = (boolean) settings.get("isTextFromTags");
        List<Map<String, Object>> tags = (List<Map<String, Object>>) settings.get("tagsWithText");
        List<String> stringTags = createListFromMap(tags);
        Map<String, Object> textFromTagExtractor = new LinkedHashMap<>();
        textFromTagExtractor.put("categories", stringCategories);
        textFromTagExtractor.put("tagsWithText", stringTags);
        if (isTextFromTags)
            params.put("textFromTagExtractor", textFromTagExtractor);
    }

    private List<String> createListFromMap(List<Map<String, Object>> categoriesTemp) {
        return categoriesTemp.stream()
                .map(elem -> (String)elem.get("name")).collect(Collectors.toList());
    }
}
