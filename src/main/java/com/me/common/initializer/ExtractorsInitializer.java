package com.me.common.initializer;

import com.me.common.MyExecutable;
import com.me.core.service.features.nGrams.NGramExtractorService;
import com.me.core.service.features.tag.TagStatExtractService;
import com.me.core.service.features.text.TextFromTagExtractorService;
import com.me.core.service.features.text.TextMainExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ExtractorsInitializer implements Initializer {

    @Lazy
    private final TextMainExtractorService textMainExtractor;
    @Lazy
    private final TextFromTagExtractorService textFromTagExtractorService;
    @Lazy
    private final NGramExtractorService nGramExtractorService;
    @Lazy
    private final TagStatExtractService tagStatExtractService;
    private final SplitterInitializer next;


    @Autowired
    public ExtractorsInitializer(TextMainExtractorService textMainExtractor,
                                 TextFromTagExtractorService textFromTagExtractorService,
                                 NGramExtractorService nGramExtractorService,
                                 TagStatExtractService tagStatExtractService, SplitterInitializer next) {
        this.textMainExtractor = textMainExtractor;
        this.textFromTagExtractorService = textFromTagExtractorService;
        this.nGramExtractorService = nGramExtractorService;
        this.tagStatExtractService = tagStatExtractService;
        this.next = next;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Map<String, Object> dto, List<MyExecutable> executables) {
        Map<String, Object> settings = (Map<String, Object>) dto.get("extract");
        if ((boolean) settings.get("isOn")) {
            List<String> stringCategories = (List<String>) settings.get("categories");
            boolean useAllCategories = (boolean) settings.get("useAllCategories");
            initTextMain(settings, useAllCategories, stringCategories, executables);
            initTextFromTags(settings, useAllCategories, stringCategories, executables);
            initNGrams(settings, useAllCategories, stringCategories, executables);
            initTagStatExtractor(settings, useAllCategories, stringCategories, executables);
        }
        next.initialize(dto, executables);
    }

    private void initTextMain(Map<String, Object> settings, boolean useAllCategories, List<String> stringCategories,
                              List<MyExecutable> executables) {
        boolean isTextMain = (boolean) settings.get("isTextMain");
        if (isTextMain) {
            textMainExtractor.setCategories(new ArrayList<>(stringCategories));
            textMainExtractor.setUseAllRelevantCategories(useAllCategories);
            executables.add(textMainExtractor);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void initTextFromTags(Map<String, Object> settings,
                                  boolean useAllCategories, List<String> stringCategories,
                                  List<MyExecutable> executables) {

        boolean isTextFromTags = (boolean) settings.get("isTextFromTags");
        if (isTextFromTags) {
            List<String> stringTags = (List<String>) settings.get("tagsWithText");
            textFromTagExtractorService.setCategories(new ArrayList<>(stringCategories));
            textFromTagExtractorService.setTags(new ArrayList<>(stringTags));
            textFromTagExtractorService.setUseAllRelevantCategories(useAllCategories);
            executables.add(textFromTagExtractorService);
        }
    }


    @SuppressWarnings("unchecked")
    private void initNGrams(Map<String, Object> settings,
                            boolean useAllCategories, List<String> stringCategories,
                            List<MyExecutable> executables) {
        boolean isNGrams = (boolean) settings.get("isNgrams");
        if (isNGrams) {
            int maxNGramSize = (int) settings.get("maxNGramSize");
            nGramExtractorService.setMaxNGramSize(maxNGramSize);
            nGramExtractorService.setCategories(new ArrayList<>(stringCategories));
            nGramExtractorService.setUseAllRelevantCategories(useAllCategories);
            executables.add(nGramExtractorService);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void initTagStatExtractor(Map<String, Object> settings,
                                      boolean useAllCategories, List<String> stringCategories, List<MyExecutable> executables) {
        boolean isTagStat = (boolean) settings.get("isTagStat");
        if (isTagStat) {
            List<String> tagsToSkipStr = (List<String>) settings.get("tagsToSkip");
            tagStatExtractService.setCategories(new ArrayList<>(stringCategories));
            tagStatExtractService.setTagsToSkip(new ArrayList<>(tagsToSkipStr));
            tagStatExtractService.setUseAllRelevantCategories(useAllCategories);
            executables.add(tagStatExtractService);
        }
    }
}