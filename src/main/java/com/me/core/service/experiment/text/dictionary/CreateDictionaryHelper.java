package com.me.core.service.experiment.text.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

public class CreateDictionaryHelper {
    /**
     * loads stopwords from file with path into vector
     */
    public static Vector<String> getStopWords(String path) {
        Vector<String> stopwords = new Vector<>();

        try (FileInputStream fstream = new FileInputStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {
            //Read File Line By Line
            String strLine;
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.replace(System.getProperty("line.separator"), "");
                if (!strLine.equals("")) {
                    stopwords.add(strLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopwords;
    }
}
