package com.me.core.service.experiment.text.output;

import com.me.core.domain.dto.AmlDatPath;
import com.me.core.domain.dto.Types;
import com.me.core.domain.entities.AmlFile;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.utils.Utils;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class AmlWriter {

    private PrintWriter writer;
    private int orderNum = 1;

    public void createExperimentAML(Experiment experiment,
                                    List<String> categories, List<AmlFile> amlFeatures,
                                    AmlDatPath amlDatPath) throws IOException {
        String amlPath = amlDatPath.getAmlPath();
        String datPath = amlDatPath.getDatPath();

        writeHeader(amlPath, datPath);
        writeBody(amlFeatures, experiment.getType());
        writeEnding(categories);
    }

    private void writeHeader(String amlPath, String datPath) throws FileNotFoundException {
        Utils.createFilePath(amlPath);
        writer = new PrintWriter(amlPath);

        // create file header
        writer.println("<?xml version=\"1.0\" encoding=\"windows-1251\" standalone=\"no\"?>");
        writer.println("<attributeset default_source=\"" + datPath + "\" encoding=\"windows-1251\">");
        writer.println("<attribute name=\"" + "siteID" + "\" sourcecol=\"" + orderNum++
                + "\" valuetype=\"integer\"/>");
    }

    private void writeBody(List<AmlFile> featuresForExperiment, Types type) {
        for (AmlFile amlFile : featuresForExperiment) {
            String feature = amlFile.getFeature();
            if (type.equals(Types.BINOMIAL)) {
                // binomial values
                writer.println("<attribute name=\"" + feature + "\" sourcecol=\"" + orderNum++
                        + "\" valuetype=\"binominal\">");
                writer.println("  <value>0</value>");
                writer.println("  <value>1</value>");
                writer.println("</attribute>");
            } else if (type.equals(Types.REAL)) {
                // then real values
                writer.println("<attribute name=\"" + feature + "\" sourcecol=\"" + orderNum++
                        + "\" valuetype=\"real\"/>");
            }
        }
    }

    private void writeEnding(List<String> categories) {
        // add length (only for main text)
        writer.println("<attribute name=\"" + "fileLength" + "\" sourcecol=\""
                + orderNum++ + "\" valuetype=\"integer\"/>");
        addCategoryLabel(categories);
        addBinomialCategories(categories);

        closeWriter();
    }

    private void addCategoryLabel(List<String> categories) {
        // add text label with category name
        for (String category : categories) {
            String categoryNum = String.valueOf((categories.indexOf(category) + 1));
            category = category.replaceAll("[^A-Z0-9a-z_]+", "_")
                    .replaceAll("__+", "_");

            if (Integer.parseInt(categoryNum) < 10)
                categoryNum = "0" + categoryNum;

            writer.println("<attribute name=\"label_" + categoryNum + '_'
                    + category
                    + "\" sourcecol=\"" + orderNum++ + "\" valuetype=\"binominal\">");
            writer.println("  <value>0</value>");
            writer.println("  <value>1</value>");
            writer.println("</attribute>");
        }
    }

    private void addBinomialCategories(List<String> categories) {
        // add general label
        writer.println("<attribute name=\"label\" sourcecol=\"" + orderNum + "\" valuetype=\"nominal\">");

        for (String category : categories) {
            writer.println("  <value>" +
                    category.substring(category.indexOf("_") + 1).replaceAll("&", "&amp;")
                            .replaceAll("[^A-Z0-9a-z_]+", "_")
                            .replaceAll("__+", "_") + "</value>");
        }
        // one extra entry for unknowns
        writer.println("  <value>Unknown</value>");
        writer.println("</attribute>");
        writer.println("</attributeset>");
    }

    private void closeWriter() {
        orderNum = 1;
        writer.close();
    }
}
