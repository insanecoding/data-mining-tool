package com.me.core.service.reporting;

import com.me.core.domain.dto.ConfusionTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
class ReportTool {

    private static Map<String, Double> sumInCol;
    private static Map<String, Double> sumInRow;
    private static Map<String, Double> truePositive;
    private static Map<String, Double> unknownsInCategory;

    private static double totalErrPredSites = 0;
    private static double totalWebsitesCount = 0;
    private static double totalCorrectProcessed = 0;
    private static double totalUnknowns = 0;
    private static double totalErrWithoutUnknown = 0;

    static void readPerformance(String excelOutput, ConfusionTable confusionTable) {

        ExcelReporter excelReporter = new ExcelReporter(excelOutput, "mySheet");
        List<String> categories = confusionTable.getCategories();
        List<List<Double>> confMat = confusionTable.getConfMat();

        calculateMatrixProperties(categories, confMat);

        prepare(excelReporter, categories, confMat);
        excelReporter.writeData();
        resetMatrixProperties();
    }

    private static void resetMatrixProperties() {
        sumInCol.clear();
        sumInRow.clear();
        truePositive.clear();
        unknownsInCategory.clear();

        totalErrPredSites = 0;
        totalWebsitesCount = 0;
        totalCorrectProcessed = 0;
        totalUnknowns = 0;
        totalErrWithoutUnknown = 0;
    }

    private static void calculateMatrixProperties(List<String> categories,
                                                  List<List<Double>> confMat) {
        sumInCol = calculateSumInCol(categories, confMat);
        sumInRow = calculateSumInRow(categories, confMat);
        truePositive = truePositives(categories, confMat);
        unknownsInCategory = unknownsInCategory(categories, confMat);
    }

    private static void prepare(ExcelReporter excelReporter,
                                List<String> categories,
                                List<List<Double>> confMat) {

        SummaryStatistics statsPrecision = new SummaryStatistics();
        SummaryStatistics statsRecall = new SummaryStatistics();

        writeHeader(excelReporter, categories, confMat);
        excelReporter.addLine(toList(""));
        writeMainCategoriesTable(excelReporter, categories, statsPrecision, statsRecall);
        excelReporter.addLine(toList(""));

        writeFooter(excelReporter, statsPrecision, statsRecall);
    }

    private static void writeHeader(ExcelReporter excelReporter,
                                    List<String> categories,
                                    List<List<Double>> confMat) {
        List<String> header = categories.stream()
                .map(category -> "true " + category)
                .collect(Collectors.toList());

        excelReporter.setManualStartIndex(1);
        excelReporter.addLine(true, header);
        excelReporter.resetStartIndex();

        for (int column = 0; column < categories.size(); column++) {
            List<Object> toInsert = new LinkedList<>();
            String currentCategory = categories.get(column);
            toInsert.add("pred. " + currentCategory);

            for (int row = 0; row < confMat.get(column).size(); row++) {
                double doubleVal = confMat.get(row).get(column);
                int intVal = (int) doubleVal;
                toInsert.add(intVal);
            }
            excelReporter.addLine(toInsert);
        }
    }

    private static void writeMainCategoriesTable(ExcelReporter excelReporter,
                                                 List<String> categories,
                                                 SummaryStatistics statsPrecision,
                                                 SummaryStatistics statsRecall) {
        excelReporter.addLine(true, toList("category", "precision", "recall", "F-measure",
                "allSites", "predSites", "correctPred", "errPred",
                "unknowns", "unknowns,%", "erWithoutUn", "erWithoutUn,%"));

        for (String category : categories) {

            double precision = truePositive.get(category) / sumInRow.get(category);
            double recall = truePositive.get(category) / sumInCol.get(category);
            double fMeasure = 2 * precision * recall / (precision + recall);

            double allSitesCount = sumInCol.get(category);
            double predStitesCount = sumInRow.get(category);
            double correctPredSitesCount = truePositive.get(category);

            double errorPredSitesCount = allSitesCount - correctPredSitesCount;
            double unknownsCount = unknownsInCategory.get(category);
            double unknownPercents = unknownsCount / allSitesCount * 100;
            double errWithoutUnknown = allSitesCount - unknownsCount - correctPredSitesCount;
            double errWithoutUnknownPercents =
                    errWithoutUnknown / (errWithoutUnknown + correctPredSitesCount) * 100;

            totalWebsitesCount += allSitesCount;
            totalCorrectProcessed += correctPredSitesCount;

            totalUnknowns += unknownsCount;
            totalErrWithoutUnknown += errWithoutUnknown;

            if (!category.equals("Unknown")) {
                totalErrPredSites += errorPredSitesCount;
                statsPrecision.addValue(precision);
                statsRecall.addValue(recall);
            }

            if (category.equalsIgnoreCase("unknown")) {
                recall = 0;
                fMeasure = 0;
                unknownPercents = 0;
                errWithoutUnknownPercents = 0;
            }

            excelReporter.addLine(toList(category, precision, recall, fMeasure,
                    allSitesCount, predStitesCount, correctPredSitesCount, errorPredSitesCount,
                    unknownsCount, unknownPercents, errWithoutUnknown, errWithoutUnknownPercents)
            );
        }
    }

    private static void writeFooter(ExcelReporter excelReporter,
                                    SummaryStatistics statsPrecision,
                                    SummaryStatistics statsRecall) {
        excelReporter.addLine(toList("totalErrPredSites", "totalSitesCount", "totalCorrProc"));
        excelReporter.addLine(toList(totalErrPredSites, totalWebsitesCount, totalCorrectProcessed));
        excelReporter.addLine(toList(""));

        excelReporter.addLine(toList("accuracy", "averPrecision",
                "averRecall", "precision std", "recall std"));
        excelReporter.addLine(toList(totalCorrectProcessed / totalWebsitesCount, statsPrecision.getMean(),
                statsRecall.getMean(), statsPrecision.getStandardDeviation(), statsRecall.getStandardDeviation()));

        excelReporter.addLine(toList(""));
        excelReporter.addLine(toList("totalUnknowns", "totalErrWithoutUnknown"));
        excelReporter.addLine(toList(totalUnknowns, totalErrWithoutUnknown));

        excelReporter.addLine(toList(""));
        excelReporter.addLine(toList("Summary:"));
        excelReporter.addLine(toList("Accuracy", "Errors", "Unknowns"));
        excelReporter.addLine(toList(totalCorrectProcessed / totalWebsitesCount,
                totalErrPredSites / totalWebsitesCount, totalUnknowns / totalWebsitesCount));


        excelReporter.addLine(toList("Accuracy without unknowns"));
        excelReporter.addLine(toList(totalCorrectProcessed / (totalWebsitesCount - totalUnknowns)));

        excelReporter.addLine(toList("Errors without unknowns"));
        excelReporter.addLine(toList(totalErrWithoutUnknown / (totalWebsitesCount - totalUnknowns)));
    }

    @NotNull
    private static List<Object> toList(Object... args) {
        return Arrays.asList(args);
    }

    @NotNull
    private static Map<String, Double> unknownsInCategory(List<String> categories,
                                                          List<List<Double>> confMat) {
        Map<String, Double> predictedUnknown = new LinkedHashMap<>();
        for (int i = 0; i < categories.size(); i++) {
            predictedUnknown.put(categories.get(i), confMat.get(i).get(categories.size() - 1));
        }
        return predictedUnknown;
    }

    @NotNull
    private static Map<String, Double> truePositives(List<String> categories,
                                                     List<List<Double>> confMat) {
        Map<String, Double> tp = new LinkedHashMap<>();
        for (int i = 0; i < categories.size(); i++) {
            tp.put(categories.get(i), confMat.get(i).get(i));
        }
        return tp;
    }

    @NotNull
    private static Map<String, Double> calculateSumInRow(List<String> categories,
                                                         List<List<Double>> confMat) {
        Map<String, Double> predictedByCategory = new LinkedHashMap<>();
        for (int column = 0; column < confMat.get(0).size(); column++) {
            int acc = 0;
            for (int i = 0; i < categories.size(); i++) {
                acc += confMat.get(i).get(column);
            }
            predictedByCategory.put(categories.get(column), (double) acc);
        }
        return predictedByCategory;
    }

    @NotNull
    private static Map<String, Double> calculateSumInCol(List<String> categories,
                                                         List<List<Double>> confMat) {
        Map<String, Double> trueByCategory = new LinkedHashMap<>();
        for (List<Double> column : confMat) {
            String categoryName = categories.get(confMat.indexOf(column));
            double sum = column.stream().reduce(0.0, Double::sum);
            trueByCategory.put(categoryName, sum);
        }
        return trueByCategory;
    }
}
