package com.me.core.service.experiment.tag.prepare_aml;

import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NormalizedTagStat implements MetricCalculator {

    private int roundToDecimalPlaces;
    private double normalizeRatio;
    private Map<String, Integer> maxTagCounts;
    private final MyDao dao;
    private List<String> dictionaryWordsStr;

    @Autowired
    public NormalizedTagStat(MyDao dao) {
        this.dao = dao;
    }

    @Override
    public void init(Experiment experiment) {
        ExperimentParam param = experiment.getExperimentParam();
        int featuresPerCategory = param.getFeaturesByCategory();

        roundToDecimalPlaces = param.getRoundToDecimalPlaces();
        normalizeRatio = param.getNormalizeRatio();
        maxTagCounts = initMaxTagCounts(experiment, featuresPerCategory);
    }

    @Override
    public void clear() {
        maxTagCounts.clear();
        dictionaryWordsStr.clear();
    }

    private Map<String, Integer> initMaxTagCounts(Experiment experiment, int featuresPerCategory) {
        List<DictionaryWords> words = dao.findDictionaryWords(experiment);
        dictionaryWordsStr = createDictionaryWordsStrList(words, featuresPerCategory);
        List<ChosenWebsite> allChosen =
                dao.findChosenWebsites(experiment.getDataSet());

        List<Long> chosenWebsiteIDs = createChosenIDsList(allChosen);

        return dao.findTagMaxCount(dictionaryWordsStr, chosenWebsiteIDs);
    }

    private List<String> createDictionaryWordsStrList(List<DictionaryWords> words, int featuresPerCategory) {
        return words.stream().limit(featuresPerCategory)
                .map(DictionaryWords::getWord)
                .map(elem -> {
                    String[] tokens = elem.split("_");
                    return tokens[tokens.length - 1];
                }).collect(Collectors.toList());
    }

    private List<Long> createChosenIDsList(List<ChosenWebsite> allChosen) {
        return allChosen.stream()
                .map(chosenWebsite -> chosenWebsite.getWebsite().getWebsiteId())
                .collect(Collectors.toList());
    }

    @Override
    public List<DatFile> calculateMetric(List<ChosenWebsite> chosen,
                                         Experiment experiment,
                                         String categoriesBasis) {

        List<DatFile> datInCategory = new LinkedList<>();

        for (ChosenWebsite chosenWs : chosen) {
            Website website = chosenWs.getWebsite();
            DatFile df = processEachWebsite(website, experiment, categoriesBasis, maxTagCounts);
            datInCategory.add(df);
        }
        return datInCategory;
    }

    private DatFile processEachWebsite(Website website, Experiment experiment,
                                       String categoriesBasis,
                                       Map<String, Integer> maxTagCounts) {
        // tag name -> tag count
        Map<String, Integer> tagInPageCount = dao.findTagInPageCount(website);
        Set<String> tagsInPage = tagInPageCount.keySet();

        String features = "";
        for (String dictionaryWord : dictionaryWordsStr) {

            if (tagsInPage.contains(dictionaryWord)) {
                features = calculateNormalizedMetrics(maxTagCounts, tagInPageCount,
                        features, dictionaryWord);
            } else {
                features += "\"0.0\" ";
            }
        }

        int length = -1;
        boolean isUnknown =
                (StringUtils.countMatches(features, "\"0.0\"") == dictionaryWordsStr.size());

        return new DatFile(categoriesBasis, features, length,
                website, experiment, isUnknown);
    }

    private String calculateNormalizedMetrics(Map<String, Integer> maxTagCounts,
                                              Map<String, Integer> tagInPageCount,
                                              String features, String tagInDictionary) {
        double maxTagOccurrence = maxTagCounts.get(tagInDictionary);
        double normalizedValue = maxTagOccurrence * normalizeRatio;
        double tagCount = tagInPageCount.get(tagInDictionary);
        double res = tagCount / normalizedValue;

        if (res > 1.0)
            res = 1.0;

        String roundedValue =
                String.format("%." + roundToDecimalPlaces + "f", res * 100)
                        .replace(",", ".");
        features += "\"" + roundedValue + "\" ";
        return features;
    }
}
