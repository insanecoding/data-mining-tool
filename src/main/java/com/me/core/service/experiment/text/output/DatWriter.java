package com.me.core.service.experiment.text.output;

import com.me.core.domain.dto.Modes;
import com.me.core.domain.dto.Types;
import com.me.core.domain.entities.DatFile;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.utils.Utils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class DatWriter {

    private PrintWriter writer;

    private void closeWriter() {
        writer.close();
    }

    void createDATForExperiment(Experiment experiment,
                                List<DatFile> subset, boolean isLearn,
                                String datPath, int categoriesNum) throws IOException {

        int unknownsNumber = categoriesNum * 1000;

        Utils.createFilePath(datPath);
        writer = new PrintWriter(datPath);

        writeExperimentData(subset);
        if (isLearn) {
            int featuresPerCategory =
                    experiment.getExperimentParam().getFeaturesByCategory();
            addUnknowns(unknownsNumber, categoriesNum, experiment, featuresPerCategory);
        }

        closeWriter();
    }

    private void writeExperimentData(List<DatFile> datFiles) {
        for (DatFile datFile: datFiles) {
            String websiteName = datFile.getWebsite().getUrl();
            websiteName = websiteName.replaceAll("[^A-Za-z]+", "");

            long id = datFile.getWebsite().getWebsiteId();
            String features = datFile.getFeatures();
            int fileLength = datFile.getLength();
            String categoriesBasis = datFile.getCategoryBasis();

            writer.println("#" + websiteName);
            writer.println("" + id + ' ' + features + fileLength + ' ' + categoriesBasis);
        }
    }

    private void addUnknowns(int unknownsNumber, int categoriesN,
                             Experiment experiment, int featuresPerCategory) {
        for (int counter = 0; counter < unknownsNumber; counter++) {
            String entryForUnknowns = "";
            // create fake website id which value is not far from max. integer size
            // valid websites with some features will never have this id
            int siteID = Integer.MAX_VALUE - 100_000 + counter;
            entryForUnknowns += ("#Unknown_" + siteID + "\n");
            entryForUnknowns += siteID + " ";

            entryForUnknowns = writeTypeFeatures(categoriesN, experiment,
                    featuresPerCategory, entryForUnknowns);

            entryForUnknowns += "0 "; // zero length
            for (int k = 0; k < categoriesN; k++) {
                entryForUnknowns += "\"0\" ";
            }

            entryForUnknowns += "Unknown\n";
            writer.print(entryForUnknowns);
        }
    }

    private String writeTypeFeatures(int categoriesN, Experiment experiment,
                                     int featuresPerCategory, String entryForUnknowns) {
        Types type = experiment.getType();
        if (type.equals(Types.REAL)) {
            for (int i = 0; i < featuresPerCategory; i++) {
                entryForUnknowns += "\"0.0\"" + " ";
            }
        } else if (type.equals(Types.BINOMIAL)){
            if (!experiment.getMode().equals(Modes.TAG_STAT)) {
                for (int i = 0; i < (categoriesN * featuresPerCategory); i++) {
                    entryForUnknowns += "\"0\"" + " ";
                }
            } else {
                for (int i = 0; i < featuresPerCategory; i++) {
                    entryForUnknowns += "\"0\"" + " ";
                }
            }
        }
        return entryForUnknowns;
    }

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
}
