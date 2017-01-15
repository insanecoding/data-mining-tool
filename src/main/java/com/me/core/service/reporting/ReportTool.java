//package com.me.core.service.reporting;
//
//import com.me.data.entities.Experiment;
//import com.me.utils.Utils;
//import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.File;
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class ReportTool {
//
//    private String workingDir;
//
//    public String getWorkingDir() {
//        return workingDir;
//    }
//
//    public void setWorkingDir(String workingDir) {
//        this.workingDir = workingDir;
//    }
//
//    private static class ConfusionTable {
//        List<String> categories;
//        List<List<Double>> confMat;
//
//        ConfusionTable(List<String> categories, List<List<Double>> confMat) {
//            this.categories = categories;
//            this.confMat = confMat;
//        }
//
//        List<String> getCategories() {
//            return categories;
//        }
//
//        List<List<Double>> getConfMat() {
//            return confMat;
//        }
//    }
//
//    private static List<List<String>> getSubNodes(Document doc, String outerTag, String innerTags) {
//        NodeList nList = doc.getElementsByTagName(outerTag);
//        List<List<String>> result = new LinkedList<>();
//
//        for (int temp = 0; temp < nList.getLength(); temp++) {
//            List<String> column = new LinkedList<>();
//            Node nNode = nList.item(temp);
//
//            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//                Element eElement = (Element) nNode;
//                NodeList nodes = eElement.getElementsByTagName(innerTags);
//                for (int count = 0; count < nodes.getLength(); count++) {
//                    Node node1 = nodes.item(count);
//                    if (node1.getNodeType() == Node.ELEMENT_NODE) {
//                        Element element = (Element) node1;
//                        column.add(element.getTextContent());
//                    }
//                }
//            }
//            result.add(column);
//        }
//        return result;
//    }
//
//    private ConfusionTable parsePerformanceXML(String performanceFile) {
//        List<List<Double>> doubleMatrix = new LinkedList<>();
//        List<String> categories = new LinkedList<>();
//
//        try {
//            File inputFile = new File(performanceFile);
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(inputFile);
//            doc.getDocumentElement().normalize();
//            List<List<String>> temp = getSubNodes(doc, "classNames", "string");
//            categories = temp.get(0);
//
//            List<List<String>> columns = getSubNodes(doc, "double-array", "double");
//
//            for (List<String> column : columns) {
//                List<Double> doubleColumn =
//                        column.stream().map(Double::parseDouble).collect(Collectors.toCollection(LinkedList::new));
//                doubleMatrix.add(doubleColumn);
//            }
//        } catch (SAXException | ParserConfigurationException | IOException e) {
//            e.printStackTrace();
//        }
//
//        return new ConfusionTable(categories, doubleMatrix);
//    }
//
//    public void readPerformance(Experiment experiment) {
//        try {
//            String fixedExpName = experiment.getExpName().replaceAll("[^A-Z0-9a-z]", "");
//            String excelOutput = workingDir + "/reports/" + fixedExpName + "/report.xlsx";
//            String performanceFile = workingDir + "/per/" + fixedExpName + "/apply.per";
//            Utils.createFilePath(excelOutput);
//
//            ExcelReporter excelReporter = new ExcelReporter(excelOutput, "mySheet");
//            ConfusionTable confusionTable = parsePerformanceXML(performanceFile);
//            List<String> categories = confusionTable.getCategories();
//            List<List<Double>> confMat = confusionTable.getConfMat();
//
//            String mask = "";
//            for (String ignored : categories) mask += "%12s";
//
//            mask += "\n";
//            String emptySpace = "       ";
//            System.out.printf("%12s", emptySpace);
//
//            System.out.printf(mask, categories.toArray());
//
//            List<String> header = new LinkedList<>();
//            categories.forEach(category -> header.add("true " + category));
//
//            excelReporter.setManualStartIndex(1);
//            excelReporter.addLine(true, header);
//            excelReporter.resetStartIndex();
//
//            for (int column = 0; column < categories.size(); column++) {
//                List<Object> toInsert = new LinkedList<>();
//                String currentCategory = categories.get(column);
//                toInsert.add("pred. " + currentCategory);
//                System.out.printf("%12s", currentCategory);
//
//                for (int row = 0; row < confMat.get(column).size(); row++) {
//                    double doubleVal = confMat.get(row).get(column);
//                    int intVal = (int) doubleVal;
//                    System.out.printf("%12d", (intVal));
//                    toInsert.add(intVal);
//                }
//                System.out.println();
//                excelReporter.addLine(toInsert);
//            }
////            double totalWebsites = 0;
////            for (List<Double> column : confMat) {
////                totalWebsites += column.stream().mapToInt(Number::intValue).sum();
////            }
//            Map<String, Double> trueByCategory = new LinkedHashMap<>();
//            for (List<Double> column : confMat) {
//                String categoryName = categories.get(confMat.indexOf(column));
//                double sum = column.stream().reduce(0.0, Double::sum);
//                trueByCategory.put(categoryName, sum);
//            }
//
//            Map<String, Double> predictedByCategory = new LinkedHashMap<>();
//            for (int column = 0; column < confMat.get(0).size(); column++) {
//                int acc = 0;
//                for (int i = 0; i < categories.size(); i++) {
//                    acc += confMat.get(i).get(column);
//                }
//                predictedByCategory.put(categories.get(column), (double) acc);
//            }
//
//            Map<String, Double> tp = new LinkedHashMap<>();
//            for (int i = 0; i < categories.size(); i++) {
//                tp.put(categories.get(i), confMat.get(i).get(i));
//            }
//
//            Map<String, Double> predictedUnknown = new LinkedHashMap<>();
//            for (int i = 0; i < categories.size(); i++) {
//                predictedUnknown.put(categories.get(i), confMat.get(i).get(categories.size() - 1));
//            }
////        System.out.println("\nTrue in category: ");
////        trueByCategory.forEach((k, v) -> System.out.println(k + " = " + v));
////        System.out.println("======");
////        System.out.println("Predicted by each category: ");
////        predictedByCategory.forEach((k, v) -> System.out.println(k + " = " + v));
////        System.out.println("======");
////        System.out.println("True positives: ");
////        tp.forEach((k, v) -> System.out.println(k + " = " + v));
////        System.out.println("======");
////        System.out.println("Pred. unknown: ");
////        predictedUnknown.forEach((k, v) -> System.out.println(k + " = " + v));
////        System.out.println("======");
////        System.out.print("Accuracy: " );
////        double sum = tp.values().stream().mapToInt(Number::intValue).sum();
////        System.out.println(sum / totalWebsites);
////        System.out.println("======");
//            System.out.println("\n");
//            excelReporter.addLine(Stream.of("").collect(Collectors.toCollection(LinkedList::new)));
//            System.out.printf("%16s%16s%16s%16s%16s%16s%16s%16s%16s%16s%16s%16s%16s\n",
//                    "category", "precision", "recall", "F-measure",
//                    "allSitesCount", "predSitesCount", "corPredSitesCnt", "errPredSitesCnt", "unrecogSitesCnt",
//                    "unknownsCount", "unknowns,%", "erW/outUnknown", "erW/outUnknwn,%");
//            excelReporter.addLine(true, Stream.of("category", "precision", "recall", "F-measure",
//                    "allSites", "predSites", "correctPred", "errPred", "unrecogn",
//                    "unknowns", "unknowns,%", "erWithoutUn", "erWithoutUn,%").collect(Collectors.toCollection(LinkedList::new)));
//
//            double totalErrPredSites = 0;
//            double totalWebsitesCount = 0;
//            double totalCorrectProcessed = 0;
//            double totalUnknowns = 0;
//            double totalUnrecognized = 0;
//            double totalErrWithoutUnknown = 0;
//            SummaryStatistics statsPrecision = new SummaryStatistics();
//            SummaryStatistics statsRecall = new SummaryStatistics();
//
//            for (String category : categories) {
//                double unrecognizedSitesCount = 0;
//
//                double precision = tp.get(category) / predictedByCategory.get(category);
//                double recall = tp.get(category) / trueByCategory.get(category);
//                double fMeasure = 2 * precision * recall / (precision + recall);
//
//                double allSitesCount = trueByCategory.get(category);
//                double predStitesCount = predictedByCategory.get(category);
//                double correctPredSitesCount = tp.get(category);
//
//                double errorPredSitesCount = predStitesCount - correctPredSitesCount;
//                double unknownsCount = predictedUnknown.get(category);
//                double unknownPercents = unknownsCount / allSitesCount * 100;
//                double errWithoutUnknown = allSitesCount - unknownsCount - correctPredSitesCount;
//                double errWithoutUnknownPercents = errWithoutUnknown / (errWithoutUnknown + correctPredSitesCount) * 100;
//
//                totalWebsitesCount += allSitesCount;
//                totalCorrectProcessed += correctPredSitesCount;
//
//                totalUnknowns += unknownsCount;
//                totalErrWithoutUnknown += errWithoutUnknown;
//
//                if (!category.equals("Unknown")) {
//                    totalErrPredSites += errorPredSitesCount;
//                    statsPrecision.addValue(precision);
//                    statsRecall.addValue(recall);
//                    unrecognizedSitesCount = allSitesCount - correctPredSitesCount;
//                    totalUnrecognized += unrecognizedSitesCount;
//                }
//
//                System.out.printf("%16s%16.4f%16.4f%16.4f%16.0f%16.0f%16.0f%16.0f%16.0f%16.0f%16.2f%16.0f%16.2f\n",
//                        category, precision, recall, fMeasure,
//                        allSitesCount, predStitesCount, correctPredSitesCount, errorPredSitesCount,
//                        unrecognizedSitesCount, unknownsCount, unknownPercents,
//                        errWithoutUnknown, errWithoutUnknownPercents);
//                if (category.equalsIgnoreCase("unknown")) {
//                    recall = 0;
//                    fMeasure = 0;
//                    unknownPercents = 0;
//                    errWithoutUnknownPercents = 0;
//                }
//
//                excelReporter.addLine(Stream.of(category, precision, recall, fMeasure,
//                        allSitesCount, predStitesCount, correctPredSitesCount, errorPredSitesCount,
//                        unrecognizedSitesCount, unknownsCount, unknownPercents,
//                        errWithoutUnknown, errWithoutUnknownPercents).collect(Collectors.toCollection(LinkedList::new)));
//            }
//            System.out.println("========================================================================================");
//            excelReporter.addLine(Stream.of("").collect(Collectors.toCollection(LinkedList::new)));
//
//            System.out.printf("%18s%18s%16s\n",
//                    "totalErrPredSites", "totalSitesCount", "totalCorrProc");
//            excelReporter.addLine(Stream.of("totalErrPredSites", "totalSitesCount", "totalCorrProc")
//                    .collect(Collectors.toCollection(LinkedList::new)));
//            System.out.printf("%18.0f%18.0f%16.0f\n",
//                    totalErrPredSites, totalWebsitesCount, totalCorrectProcessed);
//            excelReporter.addLine(Stream.of(totalErrPredSites, totalWebsitesCount, totalCorrectProcessed)
//                    .collect(Collectors.toCollection(LinkedList::new)));
//            excelReporter.addLine(Stream.of("").collect(Collectors.toCollection(LinkedList::new)));
//
//            System.out.printf("%16s%16s%16s%16s%16s\n",
//                    "accuracy", "averPrecision", "averRecall", "precision std", "recall std");
//            excelReporter.addLine(Stream.of("accuracy", "averPrecision",
//                    "averRecall", "precision std", "recall std")
//                    .collect(Collectors.toCollection(LinkedList::new)));
//            System.out.printf("%16.8f%16.4f%16.4f%16.8f%16.8f\n",
//                    totalCorrectProcessed / totalWebsitesCount, statsPrecision.getMean(),
//                    statsRecall.getMean(), statsPrecision.getStandardDeviation(), statsRecall.getStandardDeviation());
//
//            excelReporter.addLine(Stream.of(totalCorrectProcessed / totalWebsitesCount, statsPrecision.getMean(),
//                    statsRecall.getMean(), statsPrecision.getStandardDeviation(), statsRecall.getStandardDeviation())
//                    .collect(Collectors.toCollection(LinkedList::new)));
//            excelReporter.addLine(Stream.of("").collect(Collectors.toCollection(LinkedList::new)));
//
//            System.out.printf("%15s%20s%25s\n",
//                    "totalUnknowns", "totalUnrecognized", "totalErrWithoutUnknown");
//            excelReporter.addLine(Stream.of("totalUnknowns", "totalUnrecognized", "totalErrWithoutUnknown")
//                    .collect(Collectors.toCollection(LinkedList::new)));
//            // precision & recall SD - is correct ????????????
//            System.out.printf("%15.0f%20.0f%25.0f\n",
//                    totalUnknowns, totalUnrecognized, totalErrWithoutUnknown);
//            excelReporter.addLine(Stream.of(totalUnknowns, totalUnrecognized, totalErrWithoutUnknown)
//                    .collect(Collectors.toCollection(LinkedList::new)));
//
//            excelReporter.addLine(Stream.of(" ").collect(Collectors.toCollection(LinkedList::new)));
//            excelReporter.addLine(Stream.of("Summary:").collect(Collectors.toCollection(LinkedList::new)));
//            excelReporter.addLine(Stream.of("Accuracy", "Errors", "Unknowns").collect(Collectors.toCollection(LinkedList::new)));
//            excelReporter.addLine(Stream.of(totalCorrectProcessed / totalWebsitesCount,
//                    totalErrPredSites / totalWebsitesCount, totalUnknowns / totalWebsitesCount)
//                    .collect(Collectors.toCollection(LinkedList::new)));
//
//
//            excelReporter.addLine(Collections.singletonList("Accuracy without unknowns"));
//            double quantityWithoutUnknowns = totalWebsitesCount - totalUnknowns;
//            excelReporter.addLine(Collections.singletonList( ( quantityWithoutUnknowns - totalErrPredSites)
//                    / quantityWithoutUnknowns ));
//
//            excelReporter.addLine(Collections.singletonList("Errors without unknowns"));
//            excelReporter.addLine(Collections.singletonList( totalErrPredSites / quantityWithoutUnknowns ));
//
//            excelReporter.writeData();
//        } catch (Exception e) {
//            e.getMessage();
//        }
//    }
//}
