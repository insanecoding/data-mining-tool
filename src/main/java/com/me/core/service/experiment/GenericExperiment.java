//package com.me.core.service.experiment;
//
//import com.me.core.service.splitter.DataSplitter;
//import com.me.core.service.splitter.DataSplittingService;
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//import com.me.data.factory.EntitiesFactory;
//import com.me.services.experiment.text.aml.PrepareAMLDat;
//import com.me.services.experiment.text.dictionary.CreateTextDictionaryService;
//import com.me.services.experiment.text.output.AMLDATCreator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.support.GenericXmlApplicationContext;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class GenericExperiment implements InitializingBean {
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
//    public AMLDATCreator getAmldatCreator() {
//        return amldatCreator;
//    }
//
//    public void setAmldatCreator(AMLDATCreator amldatCreator) {
//        this.amldatCreator = amldatCreator;
//    }
//
//    public List<String> getTargetTags() {
//        return targetTags;
//    }
//
//    public void setTargetTags(List<String> targetTags) {
//        this.targetTags = targetTags;
//    }
//
//    public PrepareAMLDat getPrepareAMLDat() {
//        return prepareAMLDat;
//    }
//
//    public void setPrepareAMLDat(PrepareAMLDat prepareAMLDat) {
//        this.prepareAMLDat = prepareAMLDat;
//    }
//
//    public DataSet getDataSet() {
//        return dataSet;
//    }
//
//    public void setDataSet(DataSet dataSet) {
//        this.dataSet = dataSet;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
////         create categories objects from strings
//        this.categories = websiteDAO.findDesiredCategories(targetCategories);
//    }
//
//    public CreateTextDictionaryService getDictionaryService() {
//        return dictionaryService;
//    }
//
//    public void setDictionaryService(CreateTextDictionaryService dictionaryService) {
//        this.dictionaryService = dictionaryService;
//    }
//
//    public List<String> getTargetCategories() {
//        return targetCategories;
//    }
//
//    public void setTargetCategories(List<String> targetCategories) {
//        this.targetCategories = targetCategories;
//    }
//
//    public DataSplitter getDataSplitter() {
//        return dataSplitter;
//    }
//
//    public void setDataSplitter(DataSplitter dataSplitter) {
//        this.dataSplitter = dataSplitter;
//    }
//
//    public Experiment getExperiment() {
//        return experiment;
//    }
//
//    public void setExperiment(Experiment experiment) {
//        this.experiment = experiment;
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
//
//    public static void main(String[] args) {
//        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
//        ctx.load("META-INF/spring/spring-root.xml");
//        ctx.refresh();
//
//        GenericExperiment genericExperiment =
//                (GenericExperiment) ctx.getBean("genericExperiment");
//
//        List<Experiment> experiments =
//                genericExperiment.getWebsiteDAO().findExperimentsAll();
//
//        experiments.forEach(genericExperiment::writeAMLDat);
//    }
//}
