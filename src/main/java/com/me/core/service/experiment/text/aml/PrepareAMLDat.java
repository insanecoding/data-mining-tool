//package com.me.core.service.experiment.text.aml;
//
//import com.me.core.domain.entities.AbstractText;
//import com.me.core.domain.entities.Category;
//import com.me.core.domain.entities.ChosenWebsite;
//import com.me.core.domain.entities.Experiment;
//import com.me.core.service.dao.ExperimentDao;
//import com.me.core.service.experiment.Modes;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Component
//public class PrepareAMLDat {
//
//    private int[] count;
//    private AMLDATUtility AMLDATUtility;
//    private final ExperimentDao dao;
//
//    @Autowired
//    public PrepareAMLDat(ExperimentDao dao) {
//        this.dao = dao;
//    }
//
//
//    private List<? extends AbstractText> getTextsInCategory(List<? extends AbstractText> abstractTexts,
//                                                            Category category) {
//        return abstractTexts.stream()
//                .filter(
//                        text -> text.getWebsite().getCategory().getCategoryName()
//                        .equals(category.getCategoryName())
//                )
//                .collect(Collectors.toList());
//    }
//
//    public void prepareAML(Experiment experiment) {
//        AMLDATUtility.saveAMLs(experiment, count);
//    }
//
//    public void prepareDAT(Experiment experiment,
//                           List<ChosenWebsite> chosenWebsites, Map<String, String> param) {
//
//        try {
//            List<Category> chosenCategories =
//                    dao.findCategoriesByDataSet(experiment.getDataSet());
//            Modes mode = experiment.getMode();
//            List<? extends AbstractText> abstractTexts
//                    = dao.findTextsForChosenWebsites(chosenWebsites, mode, param);
//
//            for (Category category : chosenCategories) {
//                // getting texts in this category
//                List<? extends AbstractText> textsInCategory =
//                        getTextsInCategory(abstractTexts, category);
//                System.out.println("creating dat for category: " + category);
//                AMLDATUtility.saveDATs(experiment, textsInCategory, category);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//}
