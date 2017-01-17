//package com.me.core.service.experiment.tag;
//
//import com.me.core.domain.entities.*;
//import com.me.core.service.dao.MyDao;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@Component
//@Slf4j
//public class TagStatistics implements MetricCalculator {
//    @Setter @Getter
//    private int roundToDecimalPlaces;
//    private final MyDao dao;
//
//    @Autowired
//    public TagStatistics(MyDao dao) {
//        this.dao = dao;
//    }
//
//    @Override
//    public List<DatFile> calculateMetric(List<ChosenWebsite> chosenWebsites,
//                                         List<DictionaryWords> words, Experiment experiment,
//                                         String categoriesBasis) {
//        int websitesProcessed = 0;
//        // { website_id -> total tag count }
//        Map<Long, Integer> totalTagsInWebsites = dao.findTagCounts(chosenWebsites);
//        List<DatFile> datInCategory = new LinkedList<>();
//
//        for (ChosenWebsite chosenWs : chosenWebsites) {
//            Website website = chosenWs.getWebsite();
//            Map<String, Integer> tagInPageCount = dao.findTagInPageCount(website);
//            Set<String> tagsInPage = tagInPageCount.keySet();
////            List<TagsInPage> tagsInPage = websiteDAO.findTagsInPage(website);
////
////            List<String> tagsInPageStr = tagsInPage.stream()
////                    .map(tagInPage -> tagInPage.getTag().getTagName())
////                    .collect(Collectors.toList());
//            String features = "";
//            for (DictionaryWords dictionaryWord : words) {
//                String[] tokens = dictionaryWord.getWord().split("_");
//                String tagInDictionary = tokens[tokens.length - 1];
//
//                if (tagsInPage.contains(tagInDictionary)) {
//                    double totalCount = totalTagsInWebsites.get(website.getWebsiteId());
//                    double tagCount = tagInPageCount.get(tagInDictionary);
//                    double res = tagCount / totalCount;
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
//            DatFile datFile =
//                    new DatFile(categoriesBasis, features, length, website, experiment, isUnknown);
//            datInCategory.add(datFile);
//            websitesProcessed++;
//            if ((websitesProcessed % 10) == 0)
//                log.info("processed: {}", websitesProcessed);
//
//        }
//        return datInCategory;
//    }
//}
