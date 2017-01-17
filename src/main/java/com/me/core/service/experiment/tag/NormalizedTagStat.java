//package com.me.core.service.experiment.tag;
//
//import com.me.core.domain.entities.*;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//
//public class NormalizedTagStat implements MetricCalculator{
//
//    private int roundToDecimalPlaces;
//    private WebsiteDAO websiteDAO;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    private Map<String, Integer> maxTagCounts;
//    private double usageRatio;
//
//    public double getUsageRatio() {
//        return usageRatio;
//    }
//
//    public void setUsageRatio(double usageRatio) {
//        this.usageRatio = usageRatio;
//    }
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
//    private void initMaxTagCounts(Experiment experiment,
//                                  List<DictionaryWords> words) {
//        List<ChosenWebsite> allChosen =
//                websiteDAO.findChosenWebsitesByDataSet(experiment.getDataSet());
//        maxTagCounts = websiteDAO.findTagMaxCount(words, allChosen);
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public void init(Object... params) {
//        if (params.length == 2) {
//            Experiment experiment = (Experiment) params[0];
//            List<DictionaryWords> words = (List<DictionaryWords>) params[1];
//            initMaxTagCounts(experiment, words);
//        }
//    }
//
//    @Override
//    public List<DatFile> calculateMetric(List<ChosenWebsite> chosenWebsites,
//                                         List<DictionaryWords> words, Experiment experiment,
//                                         String categoriesBasis) {
//
//
//        int websitesProcessed = 0;
//        List<DatFile> datInCategory = new LinkedList<>();
//
//        for (ChosenWebsite chosenWs : chosenWebsites) {
//            Website website = chosenWs.getWebsite();
//            // tag name -> tag count
//            Map<String, Integer> tagInPageCount = websiteDAO.findTagInPageCount(website);
//            Set<String> tagsInPage = tagInPageCount.keySet();
//
//            String features = "";
//            for (DictionaryWords dictionaryWord : words) {
//                String[] tokens = dictionaryWord.getWord().split("_");
//                String tagInDictionary = tokens[tokens.length - 1];
//
//                if (tagsInPage.contains(tagInDictionary)) {
//                    double maxTagOccurrence = maxTagCounts.get(tagInDictionary);
//                    double normalizedValue = maxTagOccurrence * usageRatio;
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
