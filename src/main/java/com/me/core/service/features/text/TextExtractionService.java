//package com.me.core.service.features.text;
//
//import com.me.common.MyExecutable;
//import com.me.core.domain.entities.*;
//import com.me.core.service.dao.MyDao;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//// // TODO: 13.09.2016 : remove odd config property: categories to download
//@Slf4j
//@Component
//public class TextExtractionService implements MyExecutable {
//
//    @Getter @Setter
//    List<Category> categories;
//
//    @Getter @Setter
//    private List<Tag> tags;
//
//    @Getter @Setter
//    private MyDao dao;
//
//    @Getter @Setter
//    private final TextExtractor textExtractor;
//
//    @Autowired
//    public TextExtractionService(MyDao dao, TextExtractor textExtractor) {
//        this.dao = dao;
//        this.textExtractor = textExtractor;
//    }
//
//    public void extractMainText(){
//
//    }
//
//    public void extractTextFromTag(List<Tag> chosenTags, Category category){
//
//        List<HTML> HTMLs = dao.findHTMLsByCategory(category);
//        chosenTags.forEach(tag -> {
//            log.info("processing tag {}", tag);
//
//            List<TextFromTag> texts = new LinkedList<>();
//            HTMLs.forEach(html -> {
//                Optional<TextFromTag> textFromTag =
//                        textExtractor.extractTextFromTag(html, tag);
//                textFromTag.ifPresent(texts::add);
//            });
//            dao.batchSaveTextFromTag(texts);
//            texts.clear();
//            log.info("finished with {}", tag);
//        });
//    }
//
//    @Override
//    public void execute() throws Exception {
//
//
//        categories.forEach(category -> {
//            log.info(">> processing category {}", category);
//            extractTextFromTag(tags, category);
//            log.info("finished with category {}", category);
//        });
//    }
//
//    @Override
//    public void initialize(Map<String, Object> param) {
//
//        List<String> chosenCategories = (List<String>) param.get("categories");
//        List<String> targetTags = (List<String>) param.get("tags");
//
//        this.tags = targetTags.stream().map(Tag::new)
//                .map(tag -> dao.createOrReplaceTag(tag))
//                .collect(Collectors.toList());
//
//        this.categories = dao.findCategoriesByNames(chosenCategories);
//    }
//
//    @Override
//    public void cleanUp() {
//
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//}
