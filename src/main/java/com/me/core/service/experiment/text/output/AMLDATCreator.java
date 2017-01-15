//package com.me.core.service.experiment.text.output;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.Category;
//import com.me.data.entities.DatFile;
//import com.me.data.entities.Experiment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//
//import java.io.IOException;
//import java.util.Comparator;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class AMLDATCreator implements InitializingBean {
//    private AmlWriter amlWriter;
//    private DatWriter datWriter;
//    private WebsiteDAO websiteDAO;
//    private String path;
//    private List<String> targetCategories;
//    private List<Category> categories;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public List<String> getTargetCategories() {
//        return targetCategories;
//    }
//
//    public void setTargetCategories(List<String> targetCategories) {
//        this.targetCategories = targetCategories;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        this.categories = websiteDAO.findDesiredCategories(targetCategories);
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
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
//    public AmlWriter getAmlWriter() {
//        return amlWriter;
//    }
//
//    public void setAmlWriter(AmlWriter amlWriter) {
//        this.amlWriter = amlWriter;
//    }
//
//    public DatWriter getDatWriter() {
//        return datWriter;
//    }
//
//    public void setDatWriter(DatWriter datWriter) {
//        this.datWriter = datWriter;
//    }
//
//
//    private void writeAml(Experiment experiment, List<Category> categories,
//                          String amlPath, String datPath){
//        try {
//            amlWriter = new AmlWriter();
//            List <String> amlFeatures =
//                    websiteDAO.findAMLByExperiment(experiment);
//            amlWriter.createExperimentAML(experiment, categories, amlFeatures, amlPath, datPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void writeDat(Experiment experiment, String datPath, boolean isLearn){
//        try {
//            List<DatFile> dat =
//                    websiteDAO.findSubsetForExperiment(experiment, isLearn);
//            datWriter.createDATForExperiment(experiment, dat, isLearn, datPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void createAMLDat(Experiment experiment, String outputFolder, List<Category> categories){
//        String fileNamePart = outputFolder + "//" + experiment.getExpName().replaceAll("[^\\p{Alnum}]+", "");
//        fileNamePart += "//" + experiment.getExpName().replaceAll("[^\\p{Alnum}]+", "");
//        // learn subset
//        String aml_learn = fileNamePart + "_learn.aml";
//        String dat_learn = fileNamePart + "_learn.dat";
//        writeAml(experiment, categories, aml_learn, dat_learn);
//        logger.info("learn aml finished");
//        writeDat(experiment, dat_learn, true);
//        logger.info("learn dat finished");
//
//        // test subset
//        String aml_test = fileNamePart + "_test.aml";
//        String dat_test = fileNamePart + "_test.dat";
//        writeAml(experiment, categories, aml_test, dat_test);
//        logger.info("test aml finished");
//        writeDat(experiment, dat_test, false);
//        logger.info("test dat finished");
//    }
//
//    public void createAMLDat(Experiment experiment) {
//        createAMLDat(experiment, path, categories);
//    }
//
//    public void createAMLDat(Experiment experiment, List<Category> categories) {
//        createAMLDat(experiment, path, categories);
//    }
//
//    private void createAMLForAll(List<Experiment> experiments, String amlPath, String datPath) {
//        Map<Experiment, List<String>> expFeatures = new LinkedHashMap<>();
//        experiments.forEach(experiment -> {
//            List<String> features = websiteDAO.findAMLByExperiment(experiment);
//            expFeatures.put(experiment, features);
//        });
//        List<Category> sortCategories = categories.stream()
//                .sorted(Comparator.comparing(Category::getCategoryName))
//                .collect(Collectors.toList());
//        amlWriter.writeAMLForAll(expFeatures, sortCategories, amlPath, datPath);
//    }
//
//    private void createDatForAll(List<Experiment> experiments, String datPath, boolean isLearn){
//        try {
//            int categoriesNum = categories.size();
//            Map<Experiment,List<DatFile>> dats = new LinkedHashMap<>();
//            experiments.forEach(experiment -> {
//                List<DatFile> dat =
//                        websiteDAO.findSubsetForExperiment(experiment, isLearn);
//                dats.put(experiment, dat);
//            });
//            datWriter.createDATForAll(dats, categoriesNum, isLearn, datPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void createAMLDatForAll(List<Experiment> experiments/*, String name*/){
//        String fileNamePart = path + "//" + "united"; // + name.replaceAll("[^\\p{Alnum}]+", "");
//        fileNamePart += "//" +  "united";// + name.replaceAll("[^\\p{Alnum}]+", "");
//
//        // learn subset
//        String aml_learn = fileNamePart + "_learn.aml";
//        String dat_learn = fileNamePart + "_learn.dat";
//        createAMLForAll(experiments, aml_learn, dat_learn);
//
//        logger.info("learn aml finished");
////        createDatForAll(experiments, dat_learn, true);
////        logger.info("learn dat finished");
//
//        // test subset
//        String aml_test = fileNamePart + "_test.aml";
//        String dat_test = fileNamePart + "_test.dat";
//        createAMLForAll(experiments, aml_test, dat_test);
//
//        logger.info("test aml finished");
////        createDatForAll(experiments, dat_test, false);
////        logger.info("test dat finished");
//    }
//}
