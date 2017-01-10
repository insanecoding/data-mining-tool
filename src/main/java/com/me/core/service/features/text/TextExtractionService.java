//package com.me.core.service.features.text;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.support.GenericXmlApplicationContext;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Optional;
//
//// // TODO: 13.09.2016 : remove odd config property: categories to download
//public class TextExtractionService {
//
//    private WebsiteDAO websiteDAO;
//    private List<String> targetCategories;
//    private List<String> targetTags;
//    private TextExtractor textExtractor;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public List<String> getTargetTags() {
//        return targetTags;
//    }
//
//    public void setTargetTags(List<String> targetTags) {
//        this.targetTags = targetTags;
//    }
//
//    public TextExtractor getTextExtractor() {
//        return textExtractor;
//    }
//
//    public void setTextExtractor(TextExtractor textExtractor) {
//        this.textExtractor = textExtractor;
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
//    public List<String> getTargetCategories() {
//        return targetCategories;
//    }
//
//    public void setTargetCategories(List<String> targetCategories) {
//        this.targetCategories = targetCategories;
//    }
//
//    public void extractMainText(){
//        List<Category> categories = websiteDAO.findDesiredCategories(targetCategories);
//        logger.info(">>> starting for categories {}", categories);
//
//        categories.forEach(category -> {
//            logger.info("processing category {}", category);
//            List<HTML> HTMLs = websiteDAO.findHTMLsByCategory(category);
//            List<TextMain> textsMain = new LinkedList<>();
//
//            HTMLs.forEach(html -> {
//                Optional<TextMain> textMain = textExtractor.extractTextMain(html);
//                if (textMain.isPresent()) {
//                    textsMain.add(textMain.get());
//                }
//            });
//            websiteDAO.batchSaveTextMain(textsMain);
//            textsMain.clear();
//            logger.info("finished with {}", category);
//        });
//
//        logger.info(">>> all categories processed!");
//    }
//
//    public void extractTextFromTag(List<Tag> chosenTags, Category category){
//
//        List<HTML> HTMLs = websiteDAO.findHTMLsByCategory(category);
//        chosenTags.forEach(tag -> {
//            logger.info("processing tag {}", tag);
//
//            List<TextFromTag> texts = new LinkedList<>();
//            HTMLs.forEach(html -> {
//                Optional<TextFromTag> textFromTag =
//                        textExtractor.extractTextFromTag(html, tag);
//                if (textFromTag.isPresent()) {
//                    texts.add(textFromTag.get());
//                }
//            });
//            websiteDAO.batchSaveTextFromTag(texts);
//            texts.clear();
//            logger.info("finished with {}", tag);
//        });
//    }
//
//    public void extractTextFromTags() {
//
//        List<Tag> chosenTags = new ArrayList<>();
//        targetTags.forEach(targetTag -> {
//            Tag tag = new Tag();
//            tag.setTagName(targetTag);
//            tag = websiteDAO.createOrReplaceTag(tag);
//            chosenTags.add(tag);
//        });
//
//        List<Category> categories =
//                websiteDAO.findDesiredCategories(targetCategories);
//
//        categories.forEach(category -> {
//            logger.info(">> processing category {}", category);
//            extractTextFromTag(chosenTags, category);
//            logger.info("finished with category {}", category);
//        });
//    }
//
//    public static void main(String[] args) {
//        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
//        ctx.load("META-INF/spring/spring-root.xml");
//        ctx.refresh();
//
//        TextExtractionService textExtractionService =
//                (TextExtractionService) ctx.getBean("textMainExtractor");
////        textExtractionService.extractMainText();
//        textExtractionService.extractTextFromTags();
//    }
//}
