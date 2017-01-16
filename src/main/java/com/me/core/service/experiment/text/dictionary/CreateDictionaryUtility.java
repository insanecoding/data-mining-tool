package com.me.core.service.experiment.text.dictionary;

import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import com.me.core.service.experiment.text.stemmer_utils.StemmerService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CreateDictionaryUtility {

    @Getter @Setter
    private MyDao dao;

    private Map<String, Map<String, Float>> tfMap;
    private Map<String, Integer> dcountMap;
    private Map<String, Float> idfMap;
    private Vector<String> dictionary;


    @Autowired
    public CreateDictionaryUtility(MyDao dao) throws Exception {
        tfMap = new LinkedHashMap<>();
        dcountMap = new LinkedHashMap<>();
        idfMap = new LinkedHashMap<>();
        dictionary = new Vector<>();
        this.dao = dao;
    }

    /**
     * uses stemmer to do main text processing stuff
     */
    private String stemmString(String str) throws Exception {
        return StemmerService.stemmString(str);
    }

    /**
     * apply stemmer to texts in one category
     */
    void createLocalTFs(List<? extends AbstractText> textsInCategory, Category category,
                        boolean isTFCorrect, Experiment experiment, String stopWordsPath)
            throws Exception {
        // leave in category name letters only
        String categoryName = category.getCategoryName().replaceAll("[^A-Za-z0-9]", "");
        Map<String, Integer> termCount = new LinkedHashMap<>();
        Map<String, Float> tflocalMap = new LinkedHashMap<>();
        Integer wordsCount = 0;

        wordsCount = calculateWordCount(textsInCategory, isTFCorrect, termCount, wordsCount, stopWordsPath);

        Map<Float, Vector<String>> tmpToSortMap = new HashMap<>();

        for (String term : termCount.keySet()) {
            Float value = (float) termCount.get(term) / (float) wordsCount;
            processTerm(tmpToSortMap, term, value);
            if (dcountMap.containsKey(term)) {
                dcountMap.put(term, dcountMap.get(term) + 1);
            } else {
                dcountMap.put(term, 1);
            }
        }
        sortTempMap(tflocalMap, tmpToSortMap);
        tfMap.put(categoryName, tflocalMap);

        List<Local_tf> local_tfs = new LinkedList<>();
        for (String term : tflocalMap.keySet()) {
            Local_tf local_tf = new Local_tf(category, term.replace(".", ","),
                    tflocalMap.get(term).toString().replace(".", ","), experiment);
            local_tfs.add(local_tf);
        }
        dao.batchSave(local_tfs);
    }

    private void processTerm(Map<Float, Vector<String>> tmpToSortMap, String term, Float value) {
        if (tmpToSortMap.containsKey(value)) {
            tmpToSortMap.get(value).add(term);
        } else {
            Vector<String> vec = new Vector<>();
            vec.add(term);
            tmpToSortMap.put(value, vec);
        }
    }

    private void sortTempMap(Map<String, Float> tflocalMap, Map<Float, Vector<String>> tmpToSortMap) {
        while (tmpToSortMap.size() > 0) {
            Float maxValue = tmpToSortMap.keySet().iterator().next();
            for (Float value : tmpToSortMap.keySet()) {
                if (value > maxValue)
                    maxValue = value;
            }
            Vector<String> vec = tmpToSortMap.get(maxValue);
            for (String term : vec) {
                tflocalMap.put(term, maxValue);
            }
            tmpToSortMap.remove(maxValue);
        }
    }

    private Integer calculateWordCount(List<? extends AbstractText> textsInCategory,
                                       boolean isTFCorrect,
                                       Map<String, Integer> termCount,
                                       Integer wordsCount, String stopWordsPath) throws Exception {
        Vector<String> stopWords = CreateDictionaryHelper.getStopWords(stopWordsPath);
        for (AbstractText at : textsInCategory) {
            String text = at.getText();
            Vector<String> vec = new Vector<>();
            String fileText = text;

            fileText = fileText.replaceAll("[^A-Za-z ]+", " ");
            while (fileText.contains("  "))
                fileText = fileText.replaceAll("  +", " ");

            fileText = stemmString(fileText);

            for (String term : fileText.split(" ")) {
                if (term.length() > 2 && !stopWords.contains(term) &&
                        (!vec.contains(term) || isTFCorrect)) {
                    wordsCount++;
                    vec.add(term);
                    if (termCount.containsKey(term)) {
                        termCount.put(term, termCount.get(term) + 1);
                    } else {
                        termCount.put(term, 1);
                    }
                }
            }
        }
        return wordsCount;
    }

    /**
     * create IDF
     */
    void createIDFList(boolean isIDFcorrect, double threshold, Experiment experiment, int categoryCount) throws Exception {
        Map<Float, Vector<String>> tmpToSortMap = new HashMap<>();

        for (String term : dcountMap.keySet()) {

            Float value;
            if (isIDFcorrect) {
                value = (float) Math.log(categoryCount / (float) dcountMap.get(term));
            } else {
                Float tfsum = (float) 0.0;
                int test = 0;
                for (String s : tfMap.keySet()) {
                    Map map = tfMap.get(s);
                    if (map.containsKey(term)) {
                        Float tf = (Float) map.get(term);
                        if (tf > threshold)
                            test++;
                        tfsum += tf;
                    }
                }
                if (test != 0)
                    value = (float) Math.log(categoryCount / test);
                else
                    value = (float) 0;
            }

            processTerm(tmpToSortMap, term, value);
        }
        sortTempMap(idfMap, tmpToSortMap);

        LinkedList <IDF> idfs = new LinkedList<>();
        for (String term : idfMap.keySet()) {
            IDF idf = new IDF(experiment, term.replace(".", ","),
                    idfMap.get(term).toString().replace(".", ","));
            idfs.add(idf);
        }
        dao.batchSave(idfs);
    }

    void createTFIDFList(Category category, int categoryNum, Experiment experiment) throws Exception {
        String categoryName = category.getCategoryName();
        categoryName = categoryName.replaceAll("[^A-Za-z0-9]", "");
        Map<String, Float> tfidfMap = new LinkedHashMap<>();
        Map<String, Float> tflocalMap = tfMap.get(categoryName);

        Map<Float, Vector<String>> tmpToSortMap = new HashMap<>();
        for (String term : tflocalMap.keySet()) {
            Float value = tflocalMap.get(term) * idfMap.get(term);
            processTerm(tmpToSortMap, term, value);
        }
        sortTempMap(tfidfMap, tmpToSortMap);

        List<Local_tfidf> local_tfidfs = new LinkedList<>();
        int count = 0;
        for (String term : tfidfMap.keySet()) {
            count++;
            if (count <= 1000) {
                String leadingZero;
                if (categoryNum < 10) {
                    leadingZero = "0";
                } else {
                    leadingZero = "";
                }
                dictionary.add(leadingZero + categoryNum + " - " + categoryName + "_" + count + "_" + term);
            }

            Local_tfidf local_tfidf = new Local_tfidf(category, term.replace(".", ","),
                    tfidfMap.get(term).toString().replace(".", ","), experiment);
            local_tfidfs.add(local_tfidf);
        }
        dao.batchSave(local_tfidfs);
    }

    void saveDictionary(Experiment experiment) throws Exception {
        List <DictionaryWords> words =
                dictionary.stream()
                        .map(term -> new DictionaryWords(experiment, term))
                        .collect(Collectors.toList());
        dao.batchSave(words);
    }

    public void clear(){
        tfMap.clear();
        dcountMap.clear();
        idfMap.clear();
        dictionary.clear();
    }
}