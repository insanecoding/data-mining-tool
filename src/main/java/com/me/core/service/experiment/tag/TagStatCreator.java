//package com.me.core.service.experiment.tag;
//
//import com.me.core.domain.entities.*;
//import com.me.core.service.dao.MyDao;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.LinkedList;
//import java.util.List;
//
//@Slf4j
//@Component
//public class TagStatCreator {
//
//    @Getter @Setter
//    private MetricCalculator metricCalculator;
//    private final MyDao dao;
//
//    @Autowired
//    public TagStatCreator(MyDao dao) {
//        this.dao = dao;
//    }
//
//
//    public List<List<DatFile>> calculateTagStat(Experiment experiment,
//                                                List<DictionaryWords> words) {
//        List<List<DatFile>> datFileContents = new LinkedList<>();
//        DataSet dataSet = experiment.getDataSet();
//        List<ChosenCategory> categories = dao.findCategoriesByDataSet(dataSet);
//
//        if (metricCalculator.getClass().equals(NormalizedTagStat.class))
//            metricCalculator.init(experiment, words);
//
////        List<ChosenWebsite> chosenWebsites = websiteDAO.findChosenWebsitesByDataSet(dataSet);
//
//        categories.forEach(category -> {
//            log.info("processing category: {}", category);
//            List<ChosenWebsite> chosenWebsites =
//                    dao.findChosenWebsites(experiment.getDataSet(), category.getCategory());
////            List<TagsInPage> tagsInCategory = websiteDAO.findTagsInPage(category);
//
//            String categoriesBasis = createCategoriesBasis(categories, category);
//
//            List<DatFile> datInCategory = metricCalculator.calculateMetric(chosenWebsites,
//                    words, experiment, categoriesBasis);
//            datFileContents.add(datInCategory);
//        });
//        return datFileContents;
//    }
//
//    @NotNull
//    private String createCategoriesBasis(List<ChosenCategory> categories,
//                                         ChosenCategory category) {
//        String categoriesBasis = "";
//        for (ChosenCategory cat : categories) {
//            categoriesBasis += (cat.getCategory().getCategoryName()
//                    .equals(category.getCategory().getCategoryName()) ? "\"1\"" : "\"0\"") + " ";
//        }
//        categoriesBasis += category.getCategory().getCategoryName();
//        return categoriesBasis;
//    }
//}