//package com.me.core.service.experiment;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.support.GenericXmlApplicationContext;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class GenericExperiment {
//
//    private WebsiteDAO websiteDAO;
//    private List<String> targetCategories;
//    private List<Category> categories;
//    private Experiment experiment;
//    private DataSet dataSet;
//    private DataSplitter dataSplitter;
//    private CreateTextDictionaryService dictionaryService;
//    private PrepareAMLDat prepareAMLDat;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    private List<String> targetTags;
//    private AMLDATCreator amldatCreator;
//
//    // retrieve previously chosen websites from db
//    private List<ChosenWebsite> retrieveChosenWebsites(DataSet dataSet){
//        return websiteDAO.findChosenWebsitesByDataSet(dataSet);
//    }
//
//    private void createTextDictionary(List<ChosenWebsite> ch, Experiment experiment,
//                                      Map<String, String> param){
//        dictionaryService.createDictionary(experiment, ch, param);
//    }
//
//    private void createAMLDAT(List<ChosenWebsite> ch, Experiment experiment,
//                              Map<String, String> param){
//        prepareAMLDat.prepareAML(experiment);
//        logger.info("- aml created");
//        prepareAMLDat.prepareDAT(experiment, ch, param);
//        logger.info("- dat created");
//    }
//
//    public void splitData(DataSet dataSet){
//        DataSplittingService dataSplittingService = new DataSplittingService(dataSplitter);
//        dataSplittingService.split(dataSet, categories);
//    }
//
//    public void runTextMainExperiment(Experiment experiment){
//        runGenericTextExperiment(experiment, null);
//    }
//
//    public void runTextFromTagExperiment(Experiment experiment, String tagName){
//        Map <String, String> param = new HashMap<>();
//        param.put("tagName", tagName);
//        runGenericTextExperiment(experiment, param);
//    }
//
//    public void runNGramExperiment(Experiment experiment, int nGramLength){
//        Map <String, String> param = new HashMap<>();
//        param.put("nGramSize", String.valueOf(nGramLength));
//        runGenericTextExperiment(experiment, param);
//    }
//
//    private void runGenericTextExperiment(Experiment experiment, Map<String, String> param) {
//        // get dataSet
//        DataSet dataSet = experiment.getDataSet();
//        logger.info("start fetching chosen websites from db");
//        // retrieve previously chosen websites
//        List<ChosenWebsite> ch = retrieveChosenWebsites(dataSet);
//        logger.info("start creating dictionary");
//        createTextDictionary(ch, experiment, param);
//        logger.info("start creating aml and dat");
//        createAMLDAT(ch, experiment, param);
//    }
//
//    public void experimentProcess(){
//        DataSet dataSet = EntitiesFactory.createDataSet("set 1",
//                "16 * 1000, 80% learn");
//        splitData(dataSet);
//
//        Experiment experiment = EntitiesFactory.createExperiment("text_main",
//                "text main experiment", Modes.TEXT_MAIN, Types.BINOMIAL, dataSet);
//        runTextMainExperiment(experiment);
//
//        experiment = EntitiesFactory.createExperiment("ngrams",
//                "ngrams experiment", Modes.NGRAMS, Types.BINOMIAL, dataSet);
//        runNGramExperiment(experiment, 3);
//
//
//        List<String> tagsStr = getTargetTags();
//
////        DataSet dataSet = genericExperiment.getWebsiteDAO().findDataSet("set 1");
//        tagsStr.stream()
//                .filter(targetTag -> !targetTag.equals("title"))
//                .forEach(tag -> {
//                    Experiment experimentTag = EntitiesFactory.createExperiment("text_from_" + tag,
//                            "text from tag experiment",
//                            Modes.TEXT_FROM_TAGS, Types.BINOMIAL, dataSet);
//                    runTextFromTagExperiment(experimentTag, tag);
//                });
//
//        experiment = getWebsiteDAO().findExperimentByName("text_main");
//        writeAMLDat(experiment);
//    }
//
//    public void writeAMLDat(Experiment experiment){
//        logger.info("========================================");
//        logger.info(" >> start writing aml/dat for experiment {}", experiment.getExpName());
//        amldatCreator.createAMLDat(experiment);
//        logger.info("finished!");
//    }
//}
