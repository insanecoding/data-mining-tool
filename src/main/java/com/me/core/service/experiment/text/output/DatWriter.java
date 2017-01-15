//package com.me.core.service.experiment.text.output;
//
//
//import com.me.data.entities.DatFile;
//import com.me.data.entities.Experiment;
//import com.me.services.experiment.Modes;
//import com.me.services.experiment.Types;
//import com.me.utils.Utils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//import java.util.Map;
//
//public class DatWriter {
//
//    private PrintWriter writer;
//    private int featuresPerCategory;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public int getFeaturesPerCategory() {
//        return featuresPerCategory;
//    }
//
//    public void setFeaturesPerCategory(int featuresPerCategory) {
//        this.featuresPerCategory = featuresPerCategory;
//    }
//
//    private void closeWriter() {
//        writer.close();
//    }
//
//    private void writeExperimentData(List<DatFile> datFiles) {
//        datFiles.forEach(datFile -> {
//            String websiteName = datFile.getWebsite().getUrl();
//            websiteName = websiteName.replaceAll("[^A-Za-z]+", "");
//            long id = datFile.getWebsite().getWebsiteID();
//            String features = datFile.getFeatures();
//            int fileLength = datFile.getLength();
//            String categoriesBasis = datFile.getCategoryBasis();
//
//            writer.println("#" + websiteName);
//            writer.println("" + id + ' ' + features + fileLength + ' ' + categoriesBasis);
//        });
//    }
//
//    private void addUnknowns(int unknownsNumber, int categoriesN,
//                             Experiment experiment, int featuresPerCategory) {
//        for (int counter = 0; counter < unknownsNumber; counter++) {
//            String entryForUnknowns = "";
//            int siteID = Integer.MAX_VALUE - 100_000 + counter;
//            entryForUnknowns += ("#Unknown_" + siteID + "\n");
//            entryForUnknowns += siteID + " ";
//
//            entryForUnknowns = writeTypeFeatures(categoriesN, experiment,
//                    featuresPerCategory, entryForUnknowns);
//
//            entryForUnknowns += "0 "; // zero length
//            for (int k = 0; k < categoriesN; k++) {
//                entryForUnknowns += "\"0\" ";
//            }
//
//            entryForUnknowns += "Unknown\n";
//            writer.print(entryForUnknowns);
//        }
//    }
//
//    private String writeTypeFeatures(int categoriesN, Experiment experiment,
//                                     int featuresPerCategory, String entryForUnknowns) {
//        Types type = experiment.getType();
//        if (type.equals(Types.REAL)) {
//            for (int i = 0; i < featuresPerCategory; i++) {
//                entryForUnknowns += "\"0.0\"" + " ";
//            }
//        } else if (type.equals(Types.BINOMIAL)){
//            if (!experiment.getMode().equals(Modes.TAG_STAT)) {
//                for (int i = 0; i < (categoriesN * featuresPerCategory); i++) {
//                    entryForUnknowns += "\"0\"" + " ";
//                }
//            } else {
//                for (int i = 0; i < featuresPerCategory; i++) {
//                    entryForUnknowns += "\"0\"" + " ";
//                }
//            }
//        }
//        return entryForUnknowns;
//    }
//
//    public void createDATForExperiment(Experiment experiment,
//                                       List<DatFile> subset, boolean isLearn,
//                                       String datPath) throws IOException {
//
//        // calculate number of categories
//        int categoriesNum = (int) subset.stream()
//                .map(datFile -> datFile.getWebsite().getCategory().getCategoryName())
//                .distinct()
//                .count();
//
//        writeDat(isLearn, experiment, subset, datPath, categoriesNum);
//    }
//
//    public void createDATForAll(Map<Experiment, List<DatFile>> dats, int categoriesNum,
//                                boolean isLearn, String datPath) throws IOException {
//        int unknownsNumber = categoriesNum * 1000;
//
//        Utils.createFilePath(datPath);
//        writer = new PrintWriter(datPath);
//        dats.forEach((experiment, subset) ->
//                writeTask(isLearn, experiment, subset, categoriesNum, unknownsNumber));
//
//        closeWriter();
//    }
//
//    private void writeDat(boolean isLearn, Experiment experiment, List<DatFile> subset,
//                          String datPath, int categoriesNum) throws FileNotFoundException {
//
//        int unknownsNumber = categoriesNum * 1000;
//
//        Utils.createFilePath(datPath);
//        writer = new PrintWriter(datPath);
//        writeTask(isLearn, experiment, subset, categoriesNum, unknownsNumber);
//
//        closeWriter();
//    }
//
//    private void writeTask(boolean isLearn, Experiment experiment,
//                           List<DatFile> subset, int categoriesNum, int unknownsNumber) {
//        writeExperimentData(subset);
//        if (isLearn) {
//            addUnknowns(unknownsNumber, categoriesNum, experiment, featuresPerCategory);
//        }
//    }
//}
