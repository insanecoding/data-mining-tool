//package com.me.core.service.experiment.tag;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//import com.me.data.factory.EntitiesFactory;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public class TagStatistics implements MetricCalculator {
//
//    private int roundToDecimalPlaces;
//    private WebsiteDAO websiteDAO;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public int getRoundToDecimalPlaces() {
//        return roundToDecimalPlaces;
//    }
//
//    public void setRoundToDecimalPlaces(int roundToDecimalPlaces) {
//        this.roundToDecimalPlaces = roundToDecimalPlaces;
//    }
//
//    public WebsiteDAO getWebsiteDAO() {
//        return websiteDAO;
//    }
//
//    public void setWebsiteDAO(WebsiteDAO websiteDAO) {
//        this.websiteDAO = websiteDAO;
//    }
//
//    @Override
//    public List<DatFile> calculateMetric(List<ChosenWebsite> chosenWebsites,
//                                         List<DictionaryWords> words, Experiment experiment,
//                                         String categoriesBasis) {
//        int websitesProcessed = 0;
//        // { website_id -> total tag count }
//        Map<Long, Integer> totalTagsInWebsites = websiteDAO.findTagCounts(chosenWebsites);
//        List<DatFile> datInCategory = new LinkedList<>();
//
//        for (ChosenWebsite chosenWs : chosenWebsites) {
//            Website website = chosenWs.getWebsite();
//            Map<String, Integer> tagInPageCount = websiteDAO.findTagInPageCount(website);
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
//                    double totalCount = totalTagsInWebsites.get(website.getWebsiteID());
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
//            DatFile datFile = EntitiesFactory.createDatFile(experiment, chosenWs.getWebsite(),
//                    categoriesBasis, features, isUnknown, length);
//            datInCategory.add(datFile);
//            websitesProcessed++;
//            if ((websitesProcessed % 10) == 0) logger.info("processed: {}", websitesProcessed);
//
//        }
//        return datInCategory;
//    }
//}
