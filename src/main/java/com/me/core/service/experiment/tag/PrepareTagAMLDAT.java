//package com.me.core.service.experiment.tag;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//import org.springframework.context.support.GenericXmlApplicationContext;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class PrepareTagAMLDAT {
//    private int tagsInDictionary;
//    private TagDictionaryCreator tagDictionaryCreator;
//    private WebsiteDAO websiteDAO;
//    private TagStatCreator calculator;
//
//    public WebsiteDAO getWebsiteDAO() {
//        return websiteDAO;
//    }
//
//    public void setWebsiteDAO(WebsiteDAO websiteDAO) {
//        this.websiteDAO = websiteDAO;
//    }
//
//    public TagStatCreator getCalculator() {
//        return calculator;
//    }
//
//    public void setCalculator(TagStatCreator calculator) {
//        this.calculator = calculator;
//    }
//
//    public TagDictionaryCreator getTagDictionaryCreator() {
//        return tagDictionaryCreator;
//    }
//
//    public void setTagDictionaryCreator(TagDictionaryCreator tagDictionaryCreator) {
//        this.tagDictionaryCreator = tagDictionaryCreator;
//    }
//
//    public int getTagsInDictionary() {
//        return tagsInDictionary;
//    }
//
//    public void setTagsInDictionary(int tagsInDictionary) {
//        this.tagsInDictionary = tagsInDictionary;
//    }
//
//    public void createAML(Experiment experiment){
//        List<DictionaryWords> words = tagDictionaryCreator.createTagDictionary(experiment);
//
//        List<AmlFile> amlFiles = new LinkedList<>();
//        words.stream().limit(tagsInDictionary).forEach(word -> {
//            AmlFile amlFile = new AmlFile();
//            amlFile.setExperiment(experiment);
//            String wordStr = word.getWord().split(" - ")[1];
//            amlFile.setFeature(experiment.getExpName() + '_' + wordStr);
//            amlFiles.add(amlFile);
//        });
//        websiteDAO.batchSave(amlFiles);
//    }
//
//    public void createDAT (Experiment experiment) {
//        List<DictionaryWords> words = websiteDAO.findDictionaryWords(experiment)
//                .stream().limit(tagsInDictionary).collect(Collectors.toList());
//        List<List<DatFile>> dat = calculator.calculateTagStat(experiment, words);
//        dat.forEach(websiteDAO::batchSave);
//    }
//
//    public static void main(String[] args) {
//        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
//        ctx.load("META-INF/spring/spring-root.xml");
//        ctx.refresh();
//
//        PrepareTagAMLDAT prepareAMLDAT =
//                (PrepareTagAMLDAT) ctx.getBean("tagAmlDatCreator");
////        DataSet dataSet = prepareAMLDAT.websiteDAO.findDataSet("set 3");
////       Experiment experiment =
////                prepareAMLDAT.getWebsiteDAO().findExperimentByName("exp_5_tag_stat");
////        Experiment experiment = EntitiesFactory.createExperiment("exp_5_tag_stat",
////                "experiment with tag stat (normalized tag stat)",
////                Modes.TAG_STAT, Types.REAL, dataSet);
////        prepareAMLDAT.websiteDAO.saveExperiment(experiment);
////        prepareAMLDAT.createAML(experiment);
//
//
////        NaiveMetric naiveMetric = (NaiveMetric) ctx.getBean("naiveMetricCalculator");
////        prepareAMLDAT.getCalculator().setMetricCalculator(naiveMetric);
////        TagStatistics tagStatistics = (TagStatistics) ctx.getBean("tagStatCalculator");
////        prepareAMLDAT.getCalculator().setMetricCalculator(tagStatistics);
////      NormalizedTagStat normalizedTagStat =
////                (NormalizedTagStat) ctx.getBean("normalizedTagStat");
////        prepareAMLDAT.getCalculator().setMetricCalculator(normalizedTagStat);
////
////        prepareAMLDAT.createDAT(experiment);
//
///*        Experiment experiment =
//                prepareAMLDAT.getWebsiteDAO().findExperimentByName("exp_3_tag_stat");
//        NaiveMetric naiveMetric = (NaiveMetric) ctx.getBean("naiveMetricCalculator");
//        prepareAMLDAT.getCalculator().setMetricCalculator(naiveMetric);
//        prepareAMLDAT.createDAT(experiment);
//
//        experiment = prepareAMLDAT.getWebsiteDAO().findExperimentByName("exp_4_tag_stat");
//        TagStatistics tagStatistics = (TagStatistics) ctx.getBean("tagStatCalculator");
//        prepareAMLDAT.getCalculator().setMetricCalculator(tagStatistics);
//        prepareAMLDAT.createDAT(experiment);*/
//
//        Experiment experiment = prepareAMLDAT.getWebsiteDAO().findExperimentByName("exp_5_tag_stat");
//        NormalizedTagStat normalizedTagStat = (NormalizedTagStat) ctx.getBean("normalizedTagStat");
//        prepareAMLDAT.getCalculator().setMetricCalculator(normalizedTagStat);
//        prepareAMLDAT.createDAT(experiment);
//    }
//}
