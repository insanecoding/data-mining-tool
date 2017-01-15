//package com.me.core.service.experiment.text.dictionary;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//
//public class CreateDictionaryUtility {
//    private Vector<String> stopWords;
//    private Map<String, Map<String, Float>> tfMap;
//    private Map<String, Integer> dcountMap;
//    private Map<String, Float> idfMap;
//    private Vector<String> dictionary;
//    private int categoryCount = 0;
//    private Experiment experiment;
//    private WebsiteDAO websiteDAO;
//
//    public CreateDictionaryUtility(String stopWordsPath) throws Exception {
//        tfMap = new LinkedHashMap<>();
//        dcountMap = new LinkedHashMap<>();
//        idfMap = new LinkedHashMap<>();
//        stopWords = CreateDictionaryHelper.getStopWords(stopWordsPath);
//        dictionary = new Vector<>();
//    }
//
//    void setCategoryCount(int categoryCount) {
//        this.categoryCount = categoryCount;
//    }
//
//    public Experiment getExperiment() {
//        return experiment;
//    }
//
//    public void setExperiment(Experiment experiment) {
//        this.experiment = experiment;
//    }
//
//    public WebsiteDAO getWebsiteDAO() {
//        return websiteDAO;
//    }
//
//    public void setWebsiteDAO(WebsiteDAO websiteDAO) {
//        this.websiteDAO = websiteDAO;
//    }
//
//    /**
//     * uses stemmer to do main text processing stuff
//     */
//    private String stemmString(String str) throws IOException {
//        Stemmer s = new Stemmer();
//
//        String res = "";
//        char[] w = new char[501];
//        InputStream in = new ByteArrayInputStream(str.toLowerCase().getBytes());
//        while (true) {
//            int ch = in.read();
//            if (Character.isLetter((char) ch)) {
//                int j = 0;
//                while (true) {
//                    ch = Character.toLowerCase((char) ch);
//                    w[j] = (char) ch;
//                    if (j < 500)
//                        j++;
//                    ch = in.read();
//                    if (!Character.isLetter((char) ch)) {
//            /* to test add(char ch) */
//                        for (int c = 0; c < j; c++)
//                            s.add(w[c]);
//
//            /* or, to test add(char[] w, int j) */
//            /* s.add(w, j); */
//
//                        s.stem();
//                        {
//                            String u;
//
//              /* and now, to test toString() : */
//                            u = s.toString();
//
//              /* to test getResultBuffer(), getResultLength() : */
//              /* u = new String(s.getResultBuffer(), 0, s.getResultLength()); */
//
//                            res += u;
//                        }
//                        break;
//                    }
//                }
//            }
//            if (ch < 0)
//                break;
//            res += (char) ch;
//        }
//        return res;
//    }
//
//    /**
//     * apply stemmer to texts in one category
//     */
//    void createLocalTFs(List <? extends AbstractText> textsInCategory, Category category,
//                        boolean isTFCorrect) throws IOException {
//        // leave in category name letters only
//        String categoryName = category.getCategoryName().replaceAll("[^A-Za-z0-9]", "");
//        Map<String, Integer> termCount = new LinkedHashMap<>();
//        Map<String, Float> tflocalMap = new LinkedHashMap<>();
//        Integer wordsCount = 0;
//
//        wordsCount = calculateWordCount(textsInCategory, isTFCorrect, termCount, wordsCount);
//
//        Map<Float, Vector<String>> tmpToSortMap = new HashMap<>();
//
//        for (String term : termCount.keySet()) {
//            Float value = (float) termCount.get(term) / (float) wordsCount;
//            processTerm(tmpToSortMap, term, value);
//            if (dcountMap.containsKey(term)) {
//                dcountMap.put(term, dcountMap.get(term) + 1);
//            } else {
//                dcountMap.put(term, 1);
//            }
//        }
//        sortTempMap(tflocalMap, tmpToSortMap);
//        tfMap.put(categoryName, tflocalMap);
//
//        List<Local_tf> local_tfs = new LinkedList<>();
//        for (String term : tflocalMap.keySet()) {
//            Local_tf local_tf = new Local_tf();
//            local_tf.setExperiment(experiment);
//            local_tf.setCategory(category);
//            local_tf.setTerm(term.replace(".", ","));
//            local_tf.setValue(tflocalMap.get(term).toString().replace(".", ","));
//            local_tfs.add(local_tf);
//        }
//        websiteDAO.batchSave(local_tfs);
//        System.out.println("Done with: " + categoryName);
//    }
//
//    private void processTerm(Map<Float, Vector<String>> tmpToSortMap, String term, Float value) {
//        if (tmpToSortMap.containsKey(value)) {
//            tmpToSortMap.get(value).add(term);
//        } else {
//            Vector<String> vec = new Vector<>();
//            vec.add(term);
//            tmpToSortMap.put(value, vec);
//        }
//    }
//
//    private void sortTempMap(Map<String, Float> tflocalMap, Map<Float, Vector<String>> tmpToSortMap) {
//        while (tmpToSortMap.size() > 0) {
//            Float maxValue = tmpToSortMap.keySet().iterator().next();
//            for (Float value : tmpToSortMap.keySet()) {
//                if (value > maxValue)
//                    maxValue = value;
//            }
//            Vector<String> vec = tmpToSortMap.get(maxValue);
//            for (String term : vec) {
//                tflocalMap.put(term, maxValue);
//            }
//            tmpToSortMap.remove(maxValue);
//        }
//    }
//
//    private Integer calculateWordCount(List<? extends AbstractText> textsInCategory,
//                                       boolean isTFCorrect,
//                                       Map<String, Integer> termCount,
//                                       Integer wordsCount) throws IOException {
//        for (AbstractText at : textsInCategory) {
//            String text = at.getText();
//            Vector<String> vec = new Vector<>();
//            String fileText = text;
//
//            fileText = fileText.replaceAll("[^A-Za-z ]+", " ");
//            while (fileText.contains("  "))
//                fileText = fileText.replaceAll("  +", " ");
//
//            fileText = stemmString(fileText);
//
//            for (String term : fileText.split(" ")) {
//                if (term.length() > 2 && !stopWords.contains(term) &&
//                        (!vec.contains(term) || isTFCorrect)) {
//                    wordsCount++;
//                    vec.add(term);
//                    if (termCount.containsKey(term)) {
//                        termCount.put(term, termCount.get(term) + 1);
//                    } else {
//                        termCount.put(term, 1);
//                    }
//                }
//            }
////            System.out.println("  done. termCount.size=" + termCount.size());
//        }
//        return wordsCount;
//    }
//
//    /**
//     * create IDF
//     */
//    void createIDFList(boolean isIDFcorrect, float treshold) throws Exception {
//        Map<Float, Vector<String>> tmpToSortMap = new HashMap<>();
//
//        for (String term : dcountMap.keySet()) {
//
//            Float value;
//            if (isIDFcorrect) {
//                value = (float) Math.log(categoryCount / (float) dcountMap.get(term));
//            } else {
//                Float tfsum = (float) 0.0;
//                int test = 0;
//                for (Iterator iter = tfMap.keySet().iterator(); iter.hasNext(); ) {
//                    Map map = tfMap.get(iter.next());
//                    if (map.containsKey(term)) {
//                        Float tf = (Float) map.get(term);
//                        if (tf > treshold)
//                            test++;
//                        tfsum += tf;
//                    }
//                }
//                if (test != 0)
//                    value = (float) Math.log(categoryCount / test);
//                else
//                    value = (float) 0;
//            }
//
//            processTerm(tmpToSortMap, term, value);
//        }
//        sortTempMap(idfMap, tmpToSortMap);
//
//        LinkedList <IDF> idfs = new LinkedList<>();
//        for (String term : idfMap.keySet()) {
//            IDF idf = new IDF();
//            idf.setExperiment(experiment);
//            idf.setTerm(term.replace(".", ","));
//            idf.setValue(idfMap.get(term).toString().replace(".", ","));
//            idfs.add(idf);
//        }
//        websiteDAO.batchSave(idfs);
//    }
//
//    void createTFIDFList(Category category, int categoryNum) throws Exception {
//        String categoryName = category.getCategoryName();
//        categoryName = categoryName.replaceAll("[^A-Za-z0-9]", "");
//        Map<String, Float> tfidfMap = new LinkedHashMap<>();
//        Map<String, Float> tflocalMap = tfMap.get(categoryName);
//
//        Map<Float, Vector<String>> tmpToSortMap = new HashMap<>();
//        for (String term : tflocalMap.keySet()) {
//            Float value = tflocalMap.get(term) * idfMap.get(term);
//            processTerm(tmpToSortMap, term, value);
//        }
//        sortTempMap(tfidfMap, tmpToSortMap);
//
//        List<Local_tfidf> local_tfidfs = new LinkedList<>();
//        int count = 0;
//        for (String term : tfidfMap.keySet()) {
//            count++;
//            if (count <= 1000) {
//                String leadingZero;
//                if (categoryNum < 10) {
//                    leadingZero = "0";
//                } else {
//                    leadingZero = "";
//                }
//                dictionary.add(leadingZero + categoryNum + " - " + categoryName + "_" + count + "_" + term);
//            }
//
//            Local_tfidf local_tfidf = new Local_tfidf();
//            local_tfidf.setExperiment(experiment);
//            local_tfidf.setTerm(term.replace(".", ","));
//            local_tfidf.setValue(tfidfMap.get(term).toString().replace(".", ","));
//            local_tfidf.setCategory(category);
//            local_tfidfs.add(local_tfidf);
//        }
//        websiteDAO.batchSave(local_tfidfs);
//    }
//
//    void saveDictionary() throws Exception {
//        List <DictionaryWords> words = new LinkedList<>();
//        for (String term : dictionary) {
//            DictionaryWords dw = new DictionaryWords();
//            dw.setExperiment(experiment);
//            dw.setWord(term);
//            words.add(dw);
//        }
//        websiteDAO.batchSave(words);
//    }
//}