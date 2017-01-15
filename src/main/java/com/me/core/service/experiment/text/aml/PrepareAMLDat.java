//package com.me.core.service.experiment.text.aml;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.AbstractText;
//import com.me.data.entities.Category;
//import com.me.data.entities.ChosenWebsite;
//import com.me.data.entities.Experiment;
//import com.me.services.experiment.Modes;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class PrepareAMLDat {
//
//    private int[] count;
//    private WebsiteDAO websiteDAO;
//    private AMLDATUtility AMLDATUtility;
//
//    public AMLDATUtility getAMLDATUtility() {
//        return AMLDATUtility;
//    }
//
//    public void setAMLDATUtility(AMLDATUtility AMLDATUtility) {
//        this.AMLDATUtility = AMLDATUtility;
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
//    public int[] getCount() {
//        return count;
//    }
//
//    public void setCount(int[] count) {
//        this.count = count;
//    }
//
//    private List<? extends AbstractText> getTextsInCategory(List<? extends AbstractText> abstractTexts,
//                                                            Category category) {
//        return abstractTexts.stream()
//                .filter(text -> text.getWebsite().getCategory().getCategoryName().equals(category.getCategoryName()))
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
//                    websiteDAO.findCategoriesByDataSet(experiment.getDataSet().getName());
//            Modes mode = experiment.getMode();
//            List<? extends AbstractText> abstractTexts
//                    = websiteDAO.findTextsForChosenWebsites(chosenWebsites, mode, param);
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
