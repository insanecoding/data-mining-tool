//package com.me.core.service.experiment.tag;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.DictionaryWords;
//import com.me.data.entities.Experiment;
//import com.me.data.entities.Tag;
//import com.me.data.factory.EntitiesFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class TagDictionaryCreator {
//
//    private WebsiteDAO websiteDAO;
//    private int textsInCategory;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public int getTextsInCategory() {
//        return textsInCategory;
//    }
//
//    public void setTextsInCategory(int textsInCategory) {
//        this.textsInCategory = textsInCategory;
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
//    public List<DictionaryWords> createTagDictionary(Experiment experiment){
//        List<Tag> tags = websiteDAO.findTopTags(textsInCategory);
//        logger.info("tags fetched");
//
//        List<DictionaryWords> words = populateDictionaryWords(experiment, tags);
//        websiteDAO.batchSave(words);
//
//        logger.info("tags saved");
//        return words;
//    }
//
//    public List<DictionaryWords> populateDictionaryWords(Experiment experiment,
//                                                         List<Tag> tags) {
//        return tags.stream().map(tag -> {
//                String tagName = tag.getTagName();
//                int num = tags.indexOf(tag);
//                String word = "99 - tag_" + num + "_" + tagName;
//                return EntitiesFactory.createDictionaryWords(experiment, word);
//            }).collect(Collectors.toList());
//    }
//}
