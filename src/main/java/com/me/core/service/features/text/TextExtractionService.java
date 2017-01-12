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
//    @Override
//    public void execute() throws Exception {
//
//
//    }
//
////    @Override
////    public void initialize(Map<String, Object> param) {
////
////        List<String> chosenCategories = (List<String>) param.get("categories");
////        List<String> targetTags = (List<String>) param.get("tags");
////
////        this.tags = targetTags.stream().map(Tag::new)
////                .map(tag -> dao.createOrReplaceTag(tag))
////                .collect(Collectors.toList());
////
////        this.categories = dao.findCategoriesByNames(chosenCategories);
////    }
////
////    @Override
////    public void cleanUp() {
////
////    }
////
////    @Override
////    public String getName() {
////        return null;
////    }
//}