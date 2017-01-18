//package com.me.core.service.experiment;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//import com.me.services.experiment.text.output.AMLDATCreator;
//import com.me.utils.Utils;
//import org.apache.commons.collections4.ListUtils;
//import org.springframework.context.support.GenericXmlApplicationContext;
//
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ExperimentJoiner {
//    private WebsiteDAO websiteDAO;
//    private PrintWriter writer;
//    private String path;
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
//    private void closeWriter() {
//        writer.close();
//    }
//
//    public void joinExperiments(DataSet dataSet, List<Experiment> experiments) {
//        String datPath = path + "//aml//united//";
//        List<ChosenWebsite> chosenWebsites = websiteDAO.findChosenWebsitesByDataSet(dataSet);
//        List<ChosenWebsite> test = chosenWebsites.stream()
//                .filter(chosenWebsite -> !chosenWebsite.isForLearning())
//                .collect(Collectors.toList());
//        List<ChosenWebsite> learn = ListUtils.subtract(chosenWebsites, test);
//
//        // calculate number of categories
//        int categoriesNum = (int) test.stream()
//                .map(datFile -> datFile.getWebsite().getCategory().getCategoryName())
//                .distinct()
//                .count();
//
//        try {
//            process(experiments, datPath + "united_test.dat", test, false, categoriesNum);
//            process(experiments, datPath + "united_learn.dat", learn, true, categoriesNum);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void process(List<Experiment> experiments, String datPath,
//                        List<ChosenWebsite> subset, boolean isLearn,
//                        int categoriesNum) throws FileNotFoundException {
//        Utils.createFilePath(datPath);
//        writer = new PrintWriter(datPath);
//
//        subset.forEach(chosenWebsite -> {
//            List<DatFile> datFiles = websiteDAO.findDatForWebsite(chosenWebsite.getWebsite(), experiments);
//            writeDat(chosenWebsite.getWebsite(), datFiles);
//        });
//
//        if (isLearn) {
//            addUnknowns(categoriesNum, experiments);
//        }
//
//        closeWriter();
//    }
//
//    private void addUnknowns(int categoriesNum, List<Experiment> experiments) {
//        int unknownsNumber = categoriesNum * 1000;
//
//        for (int counter = 0; counter < unknownsNumber; counter++) {
//            String entryForUnknowns = "";
//            int siteID = Integer.MAX_VALUE - 100_000 + counter;
//            entryForUnknowns += "#Unknown_" + siteID + "\n";
//            entryForUnknowns += siteID + " ";
//
//            for (Experiment experiment : experiments) {
//                entryForUnknowns = writeTypeFeatures(categoriesNum, experiment, entryForUnknowns);
//            }
//
//            entryForUnknowns += "0 "; // zero length
//            for (int k = 0; k < categoriesNum; k++) {
//                entryForUnknowns += "\"0\" ";
//            }
//
//            entryForUnknowns += "Unknown\n";
//            writer.print(entryForUnknowns);
//        }
//    }
//
//
//    private String writeTypeFeatures(int categoriesN, Experiment experiment, String entryForUnknowns) {
//        Types type = experiment.getType();
//        if (type.equals(Types.REAL)) {
//            for (int i = 0; i < 30; i++) {
//                entryForUnknowns += "\"0.0\"" + " ";
//            }
//        } else if (type.equals(Types.BINOMIAL)){
//            if (!experiment.getMode().equals(Modes.TAG_STAT)) {
//                for (int i = 0; i < (categoriesN * 50); i++) {
//                    entryForUnknowns += "\"0\"" + " ";
//                }
//            } else {
//                for (int i = 0; i < 50; i++) {
//                    entryForUnknowns += "\"0\"" + " ";
//                }
//            }
//        }
//        return entryForUnknowns;
//    }
//
//    private void writeDat(Website website, List<DatFile> datFiles) {
//        String websiteName = website.getUrl().replaceAll("[^A-Za-z]+", "");
//        DatFile datForMainText = datFiles.get(0);
//        long id = website.getWebsiteID();
//        int fileLength = datForMainText.getLength();
//        String categoriesBasis = datForMainText.getCategoryBasis();
//
//        String features = "";
//        for (DatFile datFile : datFiles) {
//            features += datFile.getFeatures();
//        }
//        writer.println("#" + websiteName);
//        writer.println("" + id + ' ' + features + fileLength + ' ' + categoriesBasis);
//    }
//
//    public static void main(String[] args) {
//        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
//        ctx.load("META-INF/spring/spring-root.xml");
//        ctx.refresh();
//
//        ExperimentJoiner experimentJoiner = (ExperimentJoiner) ctx.getBean("experimentJoiner");
//        DataSet dataSet = experimentJoiner.getWebsiteDAO().findDataSet("set 3");
//        List<Experiment> experiments = experimentJoiner.getWebsiteDAO().findExperiments(
//                Arrays.asList("exp_3_text_main",
//                "exp_3_ngram_3",
//                "exp_3_tag_title_text",
//                "exp_3_tag_meta:description_text" ,
//                "exp_3_tag_meta:keywords_text" ,
//                "exp_3_tag_h1_text" ,
//                "exp_3_tag_h2_text" ,
//                "exp_3_tag_h3_text" ,
//                "exp_3_tag_a_text" ,
//                "exp_3_tag_img_text" ,
//                "exp_3_tag_b_text",
//                "exp_5_tag_stat")
//        );
//        List<Experiment> sorted = experiments.stream().sorted(Comparator.comparingLong(Experiment::getExperimentNumber))
//                .collect(Collectors.toList());
////        experimentJoiner.joinExperiments(dataSet, sorted);
//        experimentJoiner.amldatCreator.createAMLDatForAll(sorted);
//    }
//}
//
