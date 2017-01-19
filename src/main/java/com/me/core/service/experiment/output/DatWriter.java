package com.me.core.service.experiment.output;

import com.me.core.domain.dto.Modes;
import com.me.core.domain.dto.Types;
import com.me.core.domain.entities.ChosenWebsite;
import com.me.core.domain.entities.DatFile;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.utils.Utils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Component
public class DatWriter {

    private PrintWriter writer;

    private void closeWriter() {
        writer.close();
    }

    void createDATForExperiment(Experiment experiment, List<DatFile> subset,
                                boolean isLearn, String datPath,
                                int categoriesNum, long unknownsNum) throws IOException {

        Utils.createFilePath(datPath);
        writer = new PrintWriter(datPath);

        writeExperimentData(subset);
        if (isLearn) {
            int featuresPerCategory =
                    experiment.getExperimentParam().getFeaturesByCategory();
            addUnknowns(unknownsNum, categoriesNum, experiment, featuresPerCategory);
        }

        closeWriter();
    }

    void createDATForAll(Map<ChosenWebsite, List<DatFile>> chosen, boolean isLearn,
                         String datPath, long unknownsNumber, int categoriesNumber,
                         Map<Experiment, Integer> featuresByExperiment) throws IOException {

        Utils.createFilePath(datPath);
        writer = new PrintWriter(datPath);

        for (ChosenWebsite chosenWebsite : chosen.keySet()) {
            List<DatFile> dats = chosen.get(chosenWebsite);
            DatFile first = dats.get(0);
            String websiteName = first.getWebsite().getUrl();
            websiteName = websiteName.replaceAll("[^A-Za-z]+", "");
            long id = first.getWebsite().getWebsiteId();
            String categoriesBasis = first.getCategoryBasis();

            writer.print("#" + websiteName + "\n" + id);
            String allFeatures = "";
            int length = -1;
            for (DatFile dat : dats) {
                if (dat.getExperiment().getMode().equals(Modes.TEXT_MAIN))
                    length = dat.getLength();
                String features = dat.getFeatures();
                allFeatures += ' ' + features;
            }
            writer.println(allFeatures + ' ' + length + ' ' + categoriesBasis);
        }

        if (isLearn)
            addUnknowns(unknownsNumber, categoriesNumber, featuresByExperiment);
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

    private void addUnknowns(long unknownsNumber, int categoriesN,
                             Map<Experiment, Integer> featuresByExperiment) {
        for (int counter = 0; counter < unknownsNumber; counter++) {
            String entryForUnknowns = createUnknownsHeader(counter);

            entryForUnknowns = writeTypeFeatures(categoriesN, featuresByExperiment, entryForUnknowns);

            entryForUnknowns = createUnknownsFooter(categoriesN, entryForUnknowns);
            writer.print(entryForUnknowns);
        }
    }

    private String createUnknownsFooter(int categoriesN, String entryForUnknowns) {
        entryForUnknowns += "0 "; // zero length
        for (int k = 0; k < categoriesN; k++) {
            entryForUnknowns += "\"0\" ";
        }

        entryForUnknowns += "Unknown\n";
        return entryForUnknowns;
    }

    private String createUnknownsHeader(int counter) {
        String entryForUnknowns = "";
        // create fake website id which value is not far from max. integer size
        // valid websites with some features will never have this id
        int siteID = Integer.MAX_VALUE - 100_000 + counter;
        entryForUnknowns += ("#Unknown_" + siteID + "\n");
        entryForUnknowns += siteID + " ";
        return entryForUnknowns;
    }

    private String writeTypeFeatures(int categoriesN, Map<Experiment, Integer> featuresByExperiment,
                                     String entryForUnknowns) {
        for (Experiment experiment : featuresByExperiment.keySet()) {
            int featuresPerCategory = featuresByExperiment.get(experiment);
            entryForUnknowns = writeTypeFeatures(categoriesN, experiment,
                    featuresPerCategory, entryForUnknowns);
        }
        return entryForUnknowns;
    }

    private void addUnknowns(long unknownsNumber, int categoriesN,
                             Experiment experiment, int featuresPerCategory) {
        for (int counter = 0; counter < unknownsNumber; counter++) {
            String entryForUnknowns = createUnknownsHeader(counter);

            entryForUnknowns = writeTypeFeatures(categoriesN, experiment,
                    featuresPerCategory, entryForUnknowns);

            entryForUnknowns = createUnknownsFooter(categoriesN, entryForUnknowns);
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
}
