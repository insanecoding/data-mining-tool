package com.me.core.service.rapidminer;

import com.me.core.domain.entities.Experiment;
import com.me.core.service.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.*;

@Log
class SchemeGenerator {

    @Getter @Setter
    private String templatesPath;
    @Getter @Setter
    private String workingDir;

    void generateBaseLearnersScheme(Experiment experiment,
                                    int categoryNum, String categoryName) throws IOException {
            String baseLearnerTemplate = templatesPath + "/1)base_learner_model.rmp";
            File file = new File(baseLearnerTemplate);

            String fixedExpName = experiment.getExpName()
                    .replaceAll("[^A-Z0-9a-z]", "");

            File generated = new File(workingDir + "/schemes/" + fixedExpName +
                    "/1)textBase_" + categoryNum + ".rmp");
            Utils.createFilePath(generated.getAbsolutePath());

            if ( !generated.createNewFile() ) {
                log.info("Could not create file");
            }



            try (PrintWriter pw = new PrintWriter(generated);
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    if (line.contains("!")) {
                        int lastDelimiterPosition = line.lastIndexOf("!");
                        int firstDelimiterPosition = line.indexOf("!");
                        String forReplacement = line.substring(firstDelimiterPosition, lastDelimiterPosition + 1);
                        String betweenDelimiters = line.substring(firstDelimiterPosition + 1, lastDelimiterPosition);
                        String replacement;
                        switch (betweenDelimiters) {
                            case "learnAML": {
                                replacement = workingDir + "aml//" +
                                        fixedExpName + "//" + fixedExpName + "_learn.aml";
                                replacement = replacement.replace("\\", "\\/");
                                break;
                            }
                            case "categoryNumber": {
                                if (categoryNum < 10) {
                                    replacement = "0" + String.valueOf(categoryNum);
                                } else {
                                    replacement = String.valueOf(categoryNum);
                                }
                                line = line.replaceAll("@mode@", experiment.getExpName());
                                break;
                            }
                            case "categoryNumber@categoryName": {
                                if (categoryNum < 10) {
                                    replacement = "0" + String.valueOf(categoryNum) + '_' + categoryName;
                                } else {
                                    replacement = String.valueOf(categoryNum) + '_' + categoryName;
                                }
                                break;
                            }
                            case "text_base_categoryNumber.mod": {
                                replacement = workingDir + "/models/" +
                                        fixedExpName + "/" + "base_" + categoryNum + ".mod";
                                break;
                            }
                            case "performanceFile": {
                                replacement = workingDir + "/per/" +
                                        fixedExpName + "/" + "base_" + categoryNum + ".per";
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
            }

    }

    // manual depth and gain settings
    void generateStackingScheme(Experiment experiment, int categoriesAmount) {
        String fixedExpName = experiment.getExpName().replaceAll("[^A-Z0-9a-z]", "");
        try {
            String stackingTemplate = templatesPath + "/2)stacking.rmp";
            File file = new File(stackingTemplate);

            File generated = new File(workingDir + "/schemes/" + fixedExpName +
                    "/2)stacking.rmp");
            Utils.createFilePath(generated.getAbsolutePath());

            if ( !generated.createNewFile() ) {
                System.out.println("Could not create file");
            }
            PrintWriter pw = new PrintWriter(generated);

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            String repeatable = "";
            int threeTimesCounter = 0;

            while ((line = bufferedReader.readLine()) != null) {

                if (line.contains("!")) {
                    int lastDelimiterPosition = line.lastIndexOf("!");
                    int firstDelimiterPosition = line.indexOf("!");
                    String forReplacement = line.substring(firstDelimiterPosition, lastDelimiterPosition + 1);
                    String betweenDelimiters = line.substring(firstDelimiterPosition + 1, lastDelimiterPosition);
                    String replacement;
                    switch (betweenDelimiters) {
                        case "learnAML": {
                            replacement = workingDir + "/aml/" +
                                    fixedExpName + "/" + fixedExpName + "_learn.aml";
                            break;
                        }
                        case "text_stacking_mod": {
                            replacement = workingDir + "/models/" +
                                    fixedExpName + "/stacking.mod";
                            break;
                        }
                        case "text_stacking_per": {
                            replacement = workingDir + "/per/" +
                                    fixedExpName + "/stacking.per";
                            break;
                        }
                        case "minimal_gain": {
                            replacement = String.valueOf(0.001);
                            break;
                        }
                        case "maximal_depth": {
                            replacement = String.valueOf(20);
                            break;
                        }
                        default: {
                            replacement = "unknown";
                        }
                    }
                    pw.println(line.replaceAll(forReplacement, replacement));
                } else if (line.contains("BaseModelBlock")) {

                    repeatable = repeatable + line + System.getProperty("line.separator");
                    threeTimesCounter++;

                    if (threeTimesCounter == 3) {
                        String result = "";
                        String sub = repeatable.replaceAll(" No", "");

                        for (int i = 1; i <= categoriesAmount; i++) {
                            String replacement = workingDir + "/models/" +
                                    fixedExpName + "/" + "base_" + i + ".mod";
                            if (i == 1) {
                                result += sub.replaceAll("Base_Model", replacement);
                            }
                            else {
                                result += repeatable.replaceAll("Base_Model", replacement)
                                        .replaceAll("No", "(" + String.valueOf(i) + ")" );
                            }
                        }
                        result = result.substring(0, result.length()-2);

                        pw.println(result.replaceAll("BaseModelBlock", ""));
                    }
                } else if (line.contains("PortConnection")) {
                    String result = "";

                    for ( int i = 1; i <= categoriesAmount; i++ ) {

                        if (i == 1) {
                            result += line.replaceAll("Num", String.valueOf(i)).replaceAll(" No", "");
                            result += System.getProperty("line.separator");
                        }
                        else {
                            result += line.replaceAll("Num", String.valueOf(i))
                                    .replaceAll("No", "(" + String.valueOf(i) + ")" );
                            result += System.getProperty("line.separator");
                        }
                    }
                    result = result.substring(0, result.length()-2);
                    pw.println(result.replaceAll("PortConnection", ""));
                } else if (line.contains("SyncModel")) {
                    String result = "";

                    for ( int i = 1; i <= categoriesAmount; i++ ) {
                        result += line.replaceAll("Num", String.valueOf(i));
                        result += System.getProperty("line.separator");
                    }
                    result = result.substring(0, result.length()-2);
                    pw.println(result.replaceAll("SyncModel", ""));
                } else {
                    pw.println(line);
                }
            }
            fileReader.close();
            pw.close();
            System.out.println("Stacking scheme for " + categoriesAmount + " categories generated!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void generateApplyModelScheme(Experiment experiment) {
        String fixedExpName = experiment.getExpName().replaceAll("[^A-Z0-9a-z]", "");
        try {
            File file = new File(templatesPath + "/3)apply_model.rmp");
            File generated = new File(workingDir + "/schemes/" + fixedExpName + "/3)applyModel.rmp");

            Utils.createFilePath(generated.getAbsolutePath());

            if ( !generated.createNewFile() ) {
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
