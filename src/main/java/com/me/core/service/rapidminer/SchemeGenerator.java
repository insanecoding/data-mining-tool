package com.me.core.service.rapidminer;

import com.me.core.domain.entities.Experiment;
import com.me.core.service.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log
@Component
class SchemeGenerator {

    @Getter
    @Setter
    private String templatesPath;
    @Getter
    @Setter
    private String workingDir;

    void generateBaseLearnersScheme(String expName,
                                    int categoryNum, String categoryName) throws IOException {
        String baseLearnerTemplate = templatesPath + "\\1)base_learner_model.rmp";

        String generatedFileName = workingDir + "\\schemes\\" + expName +
                "\\1)textBase_" + categoryNum + ".rmp";
        File generated = new File(generatedFileName);

        tryCreateFile(generated);

        String content = new String(Files.readAllBytes(Paths.get(baseLearnerTemplate)));
        String learnAML = workingDir + "//amls//" + expName + "//" + expName + "_learn.aml";
        content = content.replaceAll("!learnAML!", learnAML);
        String categoryNumber = createCategoryNumber(categoryNum);

        content = content.replaceAll("!categoryNumber!", categoryNumber)
                .replaceAll("@mode@", expName);

        content = content.replaceAll("!categoryNumber@categoryName!",
                categoryNumber + "_" + categoryName);

        String rep = workingDir + "/models/" +
                expName + "/" + "base_" + categoryNum + ".mod";
        content = content.replaceAll("!text_base_categoryNumber.mod!", rep);

        String perf = workingDir + "/per/" +
                expName + "/" + "base_" + categoryNum + ".per";
        content = content.replaceAll("!performanceFile!", perf);

        Files.write(Paths.get(generatedFileName), content.getBytes());
    }

    private void tryCreateFile(File generated) throws IOException {
        Utils.createFilePath(generated.getAbsolutePath());

        if (!generated.createNewFile()) {
            log.info("Could not create file");
        }
    }

    private String createCategoryNumber(int categoryNum) {
        return (categoryNum < 10) ?
                "0" + String.valueOf(categoryNum) : String.valueOf(categoryNum);
    }

    // manual depth and gain settings
    void generateStackingScheme(String expName, int categoriesNum) throws IOException {

        double minGain = 0.001;
        int maxDepth = 20;

        String stackingTemplate = templatesPath + "/2)stacking.rmp";
        File file = new File(stackingTemplate);

        File generated = new File(workingDir + "/schemes/" + expName +
                "/2)stacking.rmp");
        tryCreateFile(generated);

        String line;
        String repeatable = "";
        int threeTimesCounter = 0;
        try (
                PrintWriter pw = new PrintWriter(generated);
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
        ) {

            while ((line = bufferedReader.readLine()) != null) {

                if (line.contains("!")) {
                    line = addMainSettings(expName, line, minGain, maxDepth);
                    pw.println(line);
                } else if (line.contains("BaseModelBlock")) {

                    repeatable = repeatable + line + System.getProperty("line.separator");
                    threeTimesCounter++;

                    if (threeTimesCounter == 3) {
                        String result = addPathsToBaseModels(expName, categoriesNum, repeatable);
                        pw.println(result.replaceAll("BaseModelBlock", ""));
                    }
                } else if (line.contains("PortConnection")) {
                    String result = connectBaseModels(categoriesNum, line);
                    pw.println(result.replaceAll("PortConnection", ""));
                } else if (line.contains("SyncModel")) {
                    String result = processSyncModel(categoriesNum, line);
                    pw.println(result.replaceAll("SyncModel", ""));
                } else {
                    pw.println(line);
                }
            }
        }
    }

    private String processSyncModel(int categoriesNum, String line) {
        String result = "";

        for (int i = 1; i <= categoriesNum; i++) {
            result += line.replaceAll("Num", String.valueOf(i));
            result += System.getProperty("line.separator");
        }
        result = result.substring(0, result.length() - 2);
        return result;
    }

    private String addMainSettings(String expName, String line, double minGain, int maxDepth) {
        String learnAml = workingDir + "/amls/" +
                expName + "/" + expName + "_learn.aml";
        line = line.replaceAll("!learnAML!", learnAml);
        String textStackingMod = workingDir + "/models/" + expName + "/stacking.mod";
        line = line.replaceAll("!text_stacking_mod!", textStackingMod);
        String per = workingDir + "/per/" + expName + "/stacking.per";
        line = line.replaceAll("!text_stacking_per!", per);
        String gain = String.valueOf(minGain);
        line = line.replaceAll("!minimal_gain!", gain);
        String depth = String.valueOf(maxDepth);
        line = line.replaceAll("!maximal_depth!", depth);
        return line;
    }

    private String connectBaseModels(int categoriesNum, String line) {
        String result = "";

        for (int i = 1; i <= categoriesNum; i++) {

            if (i == 1) {
                result += line.replaceAll("Num", String.valueOf(i)).replaceAll(" No", "");
                result += System.getProperty("line.separator");
            } else {
                result += line.replaceAll("Num", String.valueOf(i))
                        .replaceAll("No", "(" + String.valueOf(i) + ")");
                result += System.getProperty("line.separator");
            }
        }
        result = result.substring(0, result.length() - 2);
        return result;
    }

    private String addPathsToBaseModels(String expName, int categoriesAmount, String repeatable) {
        String result = "";
        String sub = repeatable.replaceAll(" No", "");

        for (int i = 1; i <= categoriesAmount; i++) {
            String replacement = workingDir + "/models/" +
                    expName + "/" + "base_" + i + ".mod";
            if (i == 1) {
                result += sub.replaceAll("Base_Model", replacement);
            } else {
                result += repeatable.replaceAll("Base_Model", replacement)
                        .replaceAll("No", "(" + String.valueOf(i) + ")");
            }
        }
        result = result.substring(0, result.length() - 2);
        return result;
    }

    void generateApplyModelScheme(Experiment experiment) {
        String fixedExpName = experiment.getExpName().replaceAll("[^A-Z0-9a-z]", "");
        try {
            File file = new File(templatesPath + "/3)apply_model.rmp");
            File generated = new File(workingDir + "/schemes/" + fixedExpName + "/3)applyModel.rmp");

            Utils.createFilePath(generated.getAbsolutePath());

            if (!generated.createNewFile()) {
                System.out.println("Could not create file");
            }

            PrintWriter pw = new PrintWriter(generated);

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                if (line.contains("!")) {
                    int lastDelimiterPosition = line.lastIndexOf("!");
                    int firstDelimiterPosition = line.indexOf("!");
                    String forReplacement = line.substring(firstDelimiterPosition, lastDelimiterPosition + 1);
                    String betweenDelimiters = line.substring(firstDelimiterPosition + 1, lastDelimiterPosition);
                    String replacement;
                    switch (betweenDelimiters) {
                        case "readModel": {
                            replacement = workingDir + "/models/" +
                                    fixedExpName + "/stacking.mod";
                            break;
                        }
                        case "testAML": {
                            replacement = workingDir + "/aml/" +
                                    fixedExpName + "/" + fixedExpName + "_test.aml";
                            break;
                        }
                        case "textPerformance": {
                            replacement = workingDir + "/per/" +
                                    fixedExpName + "/apply.per";
                            break;
                        }
                        default: {
                            replacement = "unknown";
                        }
                    }
                    pw.println(line.replaceAll(forReplacement, replacement));
                } else {
                    pw.println(line);
                }
            }
            fileReader.close();
            pw.close();
            System.out.println("Applying text model scheme generated!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
