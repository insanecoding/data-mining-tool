//package com.me.core.service.experiment.text.dictionary;
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
//public class CreateTextDictionaryService {
//
//    private CreateDictionaryUtility createdictionary;
//    private String tfSetting;
//    private String idfSetting;
//    private float treshold;     //= prop.getProperty("IDF_Treshold");
//    private String stopWordsPath;
//    private WebsiteDAO websiteDAO;
//
//    public String getTfSetting() {
//        return tfSetting;
//    }
//
//    public void setTfSetting(String tfSetting) {
//        this.tfSetting = tfSetting;
//    }
//
//    public String getIdfSetting() {
//        return idfSetting;
//    }
//
//    public void setIdfSetting(String idfSetting) {
//        this.idfSetting = idfSetting;
//    }
//
//    public float getTreshold() {
//        return treshold;
//    }
//
//    public void setTreshold(float treshold) {
//        this.treshold = treshold;
//    }
//
//    public String getStopWordsPath() {
//        return stopWordsPath;
//    }
//
//    public void setStopWordsPath(String stopWordsPath) {
//        this.stopWordsPath = stopWordsPath;
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
//    public void createDictionary(Experiment experiment,
//                                 List<ChosenWebsite> chosenWebsites, Map<String, String> param) {
//
//        try {
//            this.createdictionary = new CreateDictionaryUtility(stopWordsPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        this.createdictionary.setWebsiteDAO(websiteDAO);
//
//        boolean isTFCorrect = tfSetting.matches("[Ss]"); //= prop.getProperty("TF_Type").matches("[Ss]");
//        boolean isIDFCorrect = idfSetting.matches("[Ss]"); //= prop.getProperty("IDF_Type").matches("[Ss]");
//        Modes mode = experiment.getMode();
//
//        createdictionary.setExperiment(experiment);
//        try {
//            List<? extends AbstractText> abstractTexts
//                    = websiteDAO.findTextsForChosenWebsites(chosenWebsites, mode, param);
//
//            List <Category> chosenCategories =
//                    websiteDAO.findCategoriesByDataSet(experiment.getDataSet().getName());
//
//            for (Category category : chosenCategories) {
//                // getting texts in this category
//                List<? extends AbstractText> textsInCategory =
//                        getTextsInCategory(abstractTexts, category);
//                System.out.println("creating local tf for category: " + category);
//                createdictionary.createLocalTFs(textsInCategory, category, isTFCorrect);
//            }
//
//            createdictionary.setCategoryCount(chosenCategories.size());
//
//            System.out.println("Creating idf list");
//            createdictionary.createIDFList(isIDFCorrect, treshold);
//            System.out.println("Done.");
//
//            for (Category cat : chosenCategories) {
//                System.out.println("Creating tfidf list for " + cat);
//                createdictionary.createTFIDFList(cat, chosenCategories.indexOf(cat)+ 1);
//                System.out.println("Done.");
//            }
//            createdictionary.saveDictionary();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private List<? extends AbstractText> getTextsInCategory(List<? extends AbstractText> abstractTexts,
//                                                            Category category) {
//        return abstractTexts.stream()
//        .filter(text -> text.getWebsite().getCategory().getCategoryName().equals(category.getCategoryName()))
//        .collect(Collectors.toList());
//    }
//}
