//package com.me.core.service.experiment.tag;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//import com.me.data.factory.EntitiesFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class NaiveMetric implements MetricCalculator {
//
//    private WebsiteDAO websiteDAO;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
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
//        List<DatFile> datInCategory = new LinkedList<>();
//
//        int websitesProcessed = 0;
//        for (ChosenWebsite website : chosenWebsites) {
//            List<TagsInPage> tagsInPage =
//                    websiteDAO.findTagsInPage(website.getWebsite());
//            List<String> tagsInPageStr = tagsInPage.stream()
//                    .map(tagInPage -> tagInPage.getTag().getTagName())
//                    .collect(Collectors.toList());
//            String features = "";
//            for (DictionaryWords dictionaryWord : words) {
//                String[] tokens = dictionaryWord.getWord().split("_");
//                String tagInDictionary = tokens[tokens.length - 1];
//                features += ((tagsInPageStr.contains(tagInDictionary)) ? "\"1\"": "\"0\"") + " ";
//            }
//
//            int length = -1;
//            boolean isUnknown = !features.contains("\"1\"");
//
//            DatFile datFile = EntitiesFactory.createDatFile(experiment, website.getWebsite(),
//                    categoriesBasis, features, isUnknown, length);
//            datInCategory.add(datFile);
//            websitesProcessed++;
//            if ((websitesProcessed % 10) == 0) logger.info("processed: {}", websitesProcessed);
//
//        }
//        return datInCategory;
//    }
//}
