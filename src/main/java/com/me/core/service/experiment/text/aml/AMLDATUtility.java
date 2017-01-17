package com.me.core.service.experiment.text.aml;

import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import com.me.core.service.experiment.text.stemmer_utils.StemmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@SuppressWarnings("unchecked")
public class AMLDATUtility {

    private Vector<String>[] dictionaryWords;
    private Vector<String> categories = new Vector();
    private final MyDao dao;

    @Autowired
    public AMLDATUtility(MyDao dao) {
        this.dao = dao;
    }

    private void validateDictionaryWords(int[] count,
                                         Vector<String>[] dictionary,
                                         List<DictionaryWords> wordsFromDictionary) {
        for (DictionaryWords dWord : wordsFromDictionary) {
            String word = dWord.getWord();
            Pattern p = Pattern.compile("(\\d+) \\- ([A-Za-z&_ ]+)_(\\d+)_(.[a-z]+)");
            Matcher m = p.matcher(word);
            if (m.find(0)) {
                if (m.group(3).matches("\\d+")) {
                    processCategory(count, dictionary, m);
                }
            }
        }
    }

    private void processCategory(int[] count, Vector<String>[] dictionary, Matcher m) {
        String categoryMin = m.group(1) + "_"
                + m.group(2).replaceAll("[^A-Za-z&_]+", "_")
                .replaceAll("__+", "_");
        if (!categories.contains(categoryMin))
            categories.add(categoryMin);

        addLeadingZeros(count, dictionary, m);
    }

    private void addLeadingZeros(int[] count, Vector<String>[] dictionary, Matcher m) {
        for (int i = 0; i < count.length; i++) {
            if (Integer.parseInt(m.group(3)) <= count[i]) {
                dictionary[i].add(((m.group(1).length() < 3) ? "0" : "")
                        + ((m.group(1).length() < 2) ? "0" : "")
                        + m.group(1) + "_" + ((m.group(3).length() < 3) ? "0" : "")
                        + ((m.group(3).length() < 2) ? "0" : "")
                        + m.group(3) + "_" + m.group(4));
                this.dictionaryWords[i].add(m.group(4));
            }
        }
    }

    void saveAMLs(Experiment experiment, int[] count) throws InterruptedException {
        Vector<String>[] dictionary = initializeDataStructures(count);

        List<DictionaryWords> wordsFromDictionary =
                dao.findDictionaryWords(experiment);

        validateDictionaryWords(count, dictionary, wordsFromDictionary);

        List<AmlFile> amlFiles = addAMLsToList(experiment, count, dictionary);
        dao.batchSave(amlFiles);
    }

    private Vector<String>[] initializeDataStructures(int[] count) {
        Vector<String>[] dictionary = new Vector[count.length];
        dictionaryWords = new Vector[count.length];

        for (int i = 0; i < count.length; i++) {
            dictionary[i] = new Vector<>();
            dictionaryWords[i] = new Vector<>();
        }
        return dictionary;
    }

    private List<AmlFile> addAMLsToList(Experiment experiment, int[] count,
                                        Vector<String>[] dictionary) {
        List<AmlFile> amlFiles = new LinkedList<>();
        for (int i = 0; i < count.length; i++) {
            for (String word : dictionary[i]) {
                AmlFile amlFile =
                        new AmlFile(experiment.getExpName() + '_' + word, experiment);
                amlFiles.add(amlFile);
            }
        }
        return amlFiles;
    }

    void saveDATs(Experiment experiment,
                  List<? extends AbstractText> textsInCategory,
                  Category category) throws Exception{

        String categoryName = category.getCategoryName().replaceAll("[^A-Za-z0-9]", "");

        for (AbstractText text : textsInCategory) {
            String fileText = text.getText();
            int fileLength = fileText.length();

            fileText = processText(fileText);

            List<DatFile> datFileList =
                    createDatFilesList(experiment, categoryName, text, fileText, fileLength);
            dao.batchSave(datFileList);
        }
    }

    private String processText(String fileText) throws Exception {
        fileText = fileText.replaceAll("[^A-Za-z ]+", " ");
        fileText = fileText.replaceAll("  +", " ");
        fileText = stemmString(fileText);
        return fileText;
    }

    private List<DatFile> createDatFilesList(Experiment experiment, String categoryName,
                                             AbstractText text, String fileText, int fileLength) {
        List<DatFile> datFileList = new LinkedList<>();
        for (Vector<String> dictionaryWord : dictionaryWords) {
            String features = createFeatures(fileText, dictionaryWord);
            String categoriesBasis = createCategoryBasis(categoryName);
            boolean isUnknown = isUnknown(features);

            DatFile file = new DatFile(categoriesBasis, features,
                    fileLength, text.getWebsite(), experiment, isUnknown);
            datFileList.add(file);
        }
        return datFileList;
    }

    private String createCategoryBasis(String categoryName) {
        String categoriesBasis = "";

        for (String element : categories) {
            String categoryMin = categoryName
                    .replaceAll("[^A-Za-z_]+", "_")
                    .replaceAll("__+", "_");

            categoriesBasis += ((element.contains(categoryMin)) ? "\"1\"" : "\"0\"") + " ";
        }
        categoriesBasis += categoryName;
        return categoriesBasis;
    }

    private String createFeatures(String fileText, Vector<String> dictionaryWord) {
        String features = "";
        for (String word : dictionaryWord) {
            int count = 0;
            int offset = 0;
            int foundIndex = -1;
            while ((foundIndex = (" " + fileText + " ").indexOf(" " + word + " ", offset)) > -1) {
                count++;
                offset = foundIndex + 1;
            }
            features += ((count == 0) ? "\"0\"" : "\"1\"") + " ";
        }
        return features;
    }

    private String stemmString(String fileText) throws Exception {
        return StemmerService.stemmString(fileText);
    }

    private boolean isUnknown(String features) {
        String patternToCompare = "";
        for (Vector<String> dictionaryWord : dictionaryWords) {
            for (String ignored : dictionaryWord) {
                patternToCompare += "\"0\"" + " ";
            }
        }
        return features.equals(patternToCompare);
    }

    public void clear() {
        Arrays.stream(dictionaryWords).forEach(Vector::clear);
        categories.clear();
    }
}

