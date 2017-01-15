//package com.me.core.service.experiment.tag;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class TagStatCreator {
//
//    private WebsiteDAO websiteDAO;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    private MetricCalculator metricCalculator;
//
//    public MetricCalculator getMetricCalculator() {
//        return metricCalculator;
//    }
//
//    public void setMetricCalculator(MetricCalculator metricCalculator) {
//        this.metricCalculator = metricCalculator;
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
//    public List<List<DatFile>> calculateTagStat(Experiment experiment,
//                                                         List<DictionaryWords> words) {
//        List<List<DatFile>> datFileContents = new LinkedList<>();
//        DataSet dataSet = experiment.getDataSet();
//        List<Category> categories = websiteDAO.findCategoriesByDataSet(dataSet.getName());
//
//        if (metricCalculator.getClass().equals(NormalizedTagStat.class))
//            metricCalculator.init(experiment, words);
//
////        List<ChosenWebsite> chosenWebsites = websiteDAO.findChosenWebsitesByDataSet(dataSet);
//
//        categories.forEach(category -> {
//            logger.info("processing category: {}", category);
//            List<ChosenWebsite> chosenWebsites =
//                    websiteDAO.findChosenWebsitesByCategory(category, experiment);
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
//    private String createCategoriesBasis(List<Category> categories,
//                                         Category category) {
//        String categoriesBasis = "";
//        for (Category cat : categories) {
//            categoriesBasis += (cat.getCategoryName()
//                    .equals(category.getCategoryName()) ? "\"1\"" : "\"0\"") + " ";
//        }
//        categoriesBasis += category.getCategoryName();
//        return categoriesBasis;
//    }
//}