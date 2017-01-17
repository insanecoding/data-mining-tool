//package com.me.core.service.experiment.tag;
//
//import com.me.core.domain.entities.AmlFile;
//import com.me.core.domain.entities.DatFile;
//import com.me.core.domain.entities.DictionaryWords;
//import com.me.core.domain.entities.Experiment;
//import com.me.core.service.dao.MyDao;
//import com.me.core.service.dao.MyDaoImpl;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.support.GenericXmlApplicationContext;
//import org.springframework.stereotype.Component;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class PrepareTagAMLDAT {
//    @Getter @Setter
//    private int tagsInDictionary;
//    private TagDictionaryCreator tagDictionaryCreator;
//    private TagStatCreator calculator;
//    private final MyDao dao;
//
//    @Autowired
//    public PrepareTagAMLDAT(MyDao dao) {
//        this.dao = dao;
//    }
//
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
//        dao.batchSave(amlFiles);
//    }
//
//    public void createDAT (Experiment experiment) {
//        List<DictionaryWords> words = dao.findDictionaryWords(experiment)
//                .stream().limit(tagsInDictionary).collect(Collectors.toList());
//        List<List<DatFile>> dat = calculator.calculateTagStat(experiment, words);
//        dat.forEach(dao::batchSave);
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
