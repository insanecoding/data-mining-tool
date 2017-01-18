package com.me.core.service.experiment.tag.prepare_aml;

import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
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

    @Getter @Setter
    private int roundToDecimalPlaces;
    @Getter @Setter
    private double normalizeRatio;

    private Map<String, Integer> maxTagCounts;

    private final MyDao dao;

    @Autowired
    public NormalizedTagStat(MyDao dao) {
        this.dao = dao;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(Map<String, Object> params) {
        Experiment experiment = (Experiment) params.get("experiment");
        List<DictionaryWords> words = (List<DictionaryWords>) params.get("dictionaryWords");
        initMaxTagCounts(experiment, words);
    }

    private void initMaxTagCounts(Experiment experiment,
                                  List<DictionaryWords> words) {
        List<ChosenWebsite> allChosen =
                dao.findChosenWebsites(experiment.getDataSet());
        List<String> dictionaryWordsStr = createDictionaryWordsStrList(words);
        List<Long> chosenWebsiteIDs = createChosenIDsList(allChosen);

        maxTagCounts = dao.findTagMaxCount(dictionaryWordsStr, chosenWebsiteIDs);
    }
//    @Override
//    public List<DatFile> calculateMetric(List<ChosenWebsite> chosenWebsites,
//                                         List<DictionaryWords> words, Experiment experiment,
//                                         String categoriesBasis) {
//        List<DatFile> datInCategory = new LinkedList<>();
//
//        for (ChosenWebsite chosenWs : chosenWebsites) {
//            Website website = chosenWs.getWebsite();
//            // tag name -> tag count
//            Map<String, Integer> tagInPageCount = dao.findTagInPageCount(website);
//            Set<String> tagsInPage = tagInPageCount.keySet();
//
//            String features = "";
//            for (DictionaryWords dictionaryWord : words) {
//                String[] tokens = dictionaryWord.getWord().split("_");
//                String tagInDictionary = tokens[tokens.length - 1];
//
//                if (tagsInPage.contains(tagInDictionary)) {
//                    double maxTagOccurrence = maxTagCounts.get(tagInDictionary);
//                    double normalizedValue = maxTagOccurrence * normalizeRatio;
//                    double tagCount = tagInPageCount.get(tagInDictionary);
//                    double res = tagCount / normalizedValue;
//
//                    if (res > 1.0) res = 1.0;
//
//                    String roundedValue =
//                            String.format("%." + roundToDecimalPlaces + "f", res * 100)
//                                    .replace(",", ".");
//                    features += "\"" + roundedValue + "\" ";
//                } else {
//                    features += "\"0.0\" ";
//                }
//            }
//
//            int length = -1;
//            boolean isUnknown = (StringUtils.countMatches(features, "\"0.0\"") == words.size());
//
//            DatFile datFile = new DatFile(categoriesBasis, features, length, website, experiment, isUnknown);
//            datInCategory.add(datFile);
//
//        }
//        return datInCategory;
//    }

    @Override
    public List<DatFile> calculateMetric(Category category,
                                         List<DictionaryWords> words,
                                         Experiment experiment,
                                         String categoriesBasis) {

        List<DatFile> datInCategory = new LinkedList<>();

        // all chosen websites by category
        List<ChosenWebsite> chosen =
                dao.findChosenWebsites(experiment.getDataSet(), category);
        List<Long> chosenWebsiteIDs = createChosenIDsList(chosen);
        List<String> dictionaryWordsStr = createDictionaryWordsStrList(words);

        Map<String, Integer> maxTagCounts =
                dao.findTagMaxCount(dictionaryWordsStr, chosenWebsiteIDs);

        for (ChosenWebsite chosenWs : chosen) {
            Website website = chosenWs.getWebsite();
            DatFile df = processEachWebsite(website, experiment, categoriesBasis,
                    dictionaryWordsStr, maxTagCounts);
            datInCategory.add(df);
        }
        return datInCategory;
    }

    private DatFile processEachWebsite(Website website, Experiment experiment, String categoriesBasis,
                                       List<String> dictionaryWordsStr,
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

    private List<Long> createChosenIDsList(List<ChosenWebsite> allChosen) {
        return allChosen.stream()
                .map(chosenWebsite -> chosenWebsite.getWebsite().getWebsiteId())
                .collect(Collectors.toList());
    }

    private List<String> createDictionaryWordsStrList(List<DictionaryWords> words) {
        return words.stream()
                .map(DictionaryWords::getWord)
                .map(elem -> {
                    String[] tokens = elem.split("_");
                    return tokens[tokens.length - 1];
                }).collect(Collectors.toList());
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
