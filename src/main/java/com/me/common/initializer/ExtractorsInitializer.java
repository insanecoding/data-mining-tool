package com.me.common.initializer;

import com.me.common.MyExecutable;
import com.me.core.service.features.nGrams.NGramExtractorService;
import com.me.core.service.features.tag.TagStatExtractService;
import com.me.core.service.features.text.TextFromTagExtractorService;
import com.me.core.service.features.text.TextMainExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ExtractorsInitializer implements Initializer {


    private final TextMainExtractorService textMainExtractor;
    private final TextFromTagExtractorService textFromTagExtractorService;
    private final NGramExtractorService nGramExtractorService;
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
            initTextMain(settings, stringCategories, executables);
            initTextFromTags(settings, stringCategories, executables);
            initNGrams(settings, stringCategories, executables);
            initTagStatExtractor(settings, stringCategories, executables);
        }
        next.initialize(dto, executables);
    }

    private void initTextMain(Map<String, Object> settings, List<String> stringCategories,
                              List<MyExecutable> executables) {
        boolean isTextMain = (boolean) settings.get("isTextMain");
        if (isTextMain) {
            textMainExtractor.setCategories(new ArrayList<>(stringCategories));
            executables.add(textMainExtractor);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void initTextFromTags(Map<String, Object> settings,
                                  List<String> stringCategories,
                                  List<MyExecutable> executables) {

        boolean isTextFromTags = (boolean) settings.get("isTextFromTags");
        if (isTextFromTags) {
            List<String> stringTags = (List<String>) settings.get("tagsWithText");
            textFromTagExtractorService.setCategories(new ArrayList<>(stringCategories));
            textFromTagExtractorService.setTags(new ArrayList<>(stringTags));
            executables.add(textFromTagExtractorService);
        }
    }


    @SuppressWarnings("unchecked")
    private void initNGrams(Map<String, Object> settings,
                            List<String> stringCategories,
                            List<MyExecutable> executables) {
        boolean isNGrams = (boolean) settings.get("isNgrams");
        if (isNGrams) {
            int maxNGramSize = (int) settings.get("maxNGramSize");
            nGramExtractorService.setMaxNGramSize(maxNGramSize);
            nGramExtractorService.setCategories(new ArrayList<>(stringCategories));
            executables.add(nGramExtractorService);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void initTagStatExtractor(Map<String, Object> settings,
                                      List<String> stringCategories, List<MyExecutable> executables) {
        boolean isTagStat = (boolean) settings.get("isTagStat");
        if (isTagStat) {
            List<String> tagsToSkipStr = (List<String>) settings.get("tagsToSkip");
            tagStatExtractService.setCategories(new ArrayList<>(stringCategories));
            tagStatExtractService.setTagsToSkip(new ArrayList<>(tagsToSkipStr));
            executables.add(tagStatExtractService);
        }
    }
}