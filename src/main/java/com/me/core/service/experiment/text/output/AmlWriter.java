//package com.me.core.service.experiment.text.output;
//
//import com.me.data.entities.Category;
//import com.me.data.entities.Experiment;
//import com.me.services.experiment.Types;
//import com.me.utils.Utils;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class AmlWriter {
//
//    private PrintWriter writer;
//    private int orderNum;
//
//    public AmlWriter() throws IOException {
//        this.orderNum = 1;
//    }
//
//    private void closeWriter() {
//        orderNum = 1;
//        writer.close();
//    }
//
//    private void writeAMLHeader(String datFileName) {
//        // create file header
//        writer.println("<?xml version=\"1.0\" encoding=\"windows-1251\" standalone=\"no\"?>");
//        writer.println("<attributeset default_source=\"" + datFileName + "\" encoding=\"windows-1251\">");
//        writer.println("<attribute name=\"" + "siteID" + "\" sourcecol=\"" + orderNum++
//                + "\" valuetype=\"integer\"/>");
//    }
//
//    private void writeBody(List<String> featuresForExperiment, Types type) {
//        for (String feature : featuresForExperiment) {
//            if (type.equals(Types.BINOMIAL)) {
//                // binomial values
//                writer.println("<attribute name=\"" + feature + "\" sourcecol=\"" + orderNum++
//                        + "\" valuetype=\"binominal\">");
//                writer.println("  <value>0</value>");
//                writer.println("  <value>1</value>");
//                writer.println("</attribute>");
//            } else if (type.equals(Types.REAL)) {
//                // then real values
//                writer.println("<attribute name=\"" + feature + "\" sourcecol=\"" + orderNum++
//                        + "\" valuetype=\"real\"/>");
//            }
//        }
//    }
//
//    private void writeAMLEnding(List<String> categories) {
//        // add length (only for main text)
//        writer.println("<attribute name=\"" + "fileLength" + "\" sourcecol=\""
//                + orderNum++ + "\" valuetype=\"integer\"/>");
//
//        // add category label
//        for (String category : categories) {
//            String categoryNum = String.valueOf((categories.indexOf(category) + 1));
//            category = category.replaceAll("[^A-Z0-9a-z_]+", "_").replaceAll("__+", "_");
//
//            if (Integer.parseInt(categoryNum) < 10)
//                categoryNum = "0" + categoryNum;
//
//            writer.println("<attribute name=\"label_" + categoryNum + '_'
//                    + category
//                    + "\" sourcecol=\"" + orderNum++ + "\" valuetype=\"binominal\">");
//            writer.println("  <value>0</value>");
//            writer.println("  <value>1</value>");
//            writer.println("</attribute>");
//        }
//
//        // add general label
//        writer.println("<attribute name=\"label\" sourcecol=\"" + orderNum + "\" valuetype=\"nominal\">");
//
//        for (String category : categories) {
//            writer.println("  <value>" +
//                    category.substring(category.indexOf("_") + 1).replaceAll("&", "&amp;")
//                            .replaceAll("[^A-Z0-9a-z_]+", "_")
//                            .replaceAll("__+", "_") + "</value>");
//        }
//        writer.println("  <value>Unknown</value>");
//        writer.println("</attribute>");
//        writer.println("</attributeset>");
//    }
//
//
//    public void createExperimentAML(Experiment experiment,
//                                    List<Category> categories, List<String> amlFeatures,
//                                    String amlPath, String datPath) throws IOException {
//        writeHeader(amlPath, datPath);
//        writeBody(amlFeatures, experiment.getType());
//        writeEnding(categories);
//    }
//
//    private void writeEnding(List<Category> categories) {
//        List<String> strCategories = categories.stream()
//                .map(Category::getCategoryName)
//                .collect(Collectors.toList());
//        writeAMLEnding(strCategories);
//
//        closeWriter();
//    }
//
//    private void writeHeader(String amlPath, String datPath) throws FileNotFoundException {
//        Utils.createFilePath(amlPath);
//        writer = new PrintWriter(amlPath);
//
//        writeAMLHeader(datPath);
//    }
//
//    public void writeAMLForAll(Map<Experiment,List<String>> expFeatures, List<Category> categories,
//                               String amlPath, String datPath){
//        try {
//            writeHeader(amlPath, datPath);
//            expFeatures.forEach((exp, features) -> writeBody(features, exp.getType()));
//            writeEnding(categories);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//}
