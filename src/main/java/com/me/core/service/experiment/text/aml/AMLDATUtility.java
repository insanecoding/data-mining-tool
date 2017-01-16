//package com.me.core.service.experiment.text.aml;
//
//import com.me.core.domain.entities.*;
//import com.me.core.service.dao.ExperimentDao;
//import com.me.core.service.experiment.text.stemmer_utils.StemmerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Vector;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Component
//@SuppressWarnings("unchecked")
//public class AMLDATUtility {
//
//    private Vector<String>[] dictionaryWords;
//    private Vector<String> categories = new Vector();
//    private final ExperimentDao dao;
//
//    @Autowired
//    public AMLDATUtility(ExperimentDao dao) {
//        this.dao = dao;
//    }
//
//    private void validateDictionaryWords(int[] count,
//                                         Vector<String>[] dictionary,
//                                         List<DictionaryWords> wordsFromDictionary) {
//        for (DictionaryWords dWord : wordsFromDictionary) {
//            String word = dWord.getWord();
//            Pattern p = Pattern.compile("(\\d+) \\- ([A-Za-z&_ ]+)_(\\d+)_(.[a-z]+)");
//            Matcher m = p.matcher(word);
//            if (m.find(0)) {
//                if (m.group(3).matches("\\d+")) {
//                    String categoryMin = m.group(1) + "_"
//                            + m.group(2).replaceAll("[^A-Za-z&_]+", "_")
//                            .replaceAll("__+", "_");
//                    if (!categories.contains(categoryMin))
//                        categories.add(categoryMin);
//
//                    for (int i = 0; i < count.length; i++) {
//                        if (Integer.parseInt(m.group(3)) <= count[i]) {
//                            dictionary[i].add(((m.group(1).length() < 3) ? "0" : "")
//                                    + ((m.group(1).length() < 2) ? "0" : "")
//                                    + m.group(1) + "_" + ((m.group(3).length() < 3) ? "0" : "")
//                                    + ((m.group(3).length() < 2) ? "0" : "")
//                                    + m.group(3) + "_" + m.group(4));
//                            this.dictionaryWords[i].add(m.group(4));
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    public void saveAMLs(Experiment experiment, int[] count) {
//        Vector<String>[] dictionary = new Vector[count.length];
//        dictionaryWords = new Vector[count.length];
//
//        for (int i = 0; i < count.length; i++) {
//            dictionary[i] = new Vector<>();
//            dictionaryWords[i] = new Vector<>();
//        }
//
//        List<DictionaryWords> wordsFromDictionary =
//                dao.findDictionaryWords(experiment);
//
//        validateDictionaryWords(count, dictionary, wordsFromDictionary);
//
//        List<AmlFile> amlFiles = new LinkedList<>();
//        for (int i = 0; i < count.length; i++) {
//            for (String word : dictionary[i]) {
//                AmlFile amlFile =
//                        new AmlFile(experiment.getExpName() + '_' + word, experiment);
//                amlFiles.add(amlFile);
//            }
//        }
//        dao.batchSave(amlFiles);
//    }
//
//    public void saveDATs(Experiment experiment,
//                         List <? extends AbstractText> textsInCategory,
//                         Category category) throws Exception{
//        // map namedText: key = website_url; value = text_contents
//        // namedText contains data only belonging to categoryName
//        String categoryName = category.getCategoryName().replaceAll("[^A-Za-z0-9]", "");
//
//        for (AbstractText text : textsInCategory) {
//            String fileText = text.getText();
//            int fileLength = fileText.length();
//
//            fileText = fileText.replaceAll("[^A-Za-z ]+", " ");
//            fileText = fileText.replaceAll("  +", " ");
//            fileText = stemmString(fileText);
//
//            List<DatFile> datFileList = new LinkedList<>();
//            for (Vector<String> dictionaryWord : dictionaryWords) {
//                DatFile datFile = new DatFile();
//                datFile.setExperiment(experiment);
//                datFile.setWebsite(text.getWebsite());
//
//                String features = "";
//                for (String word : dictionaryWord) {
//                    int count = 0;
//                    int offset = 0;
//                    int foundIndex = -1;
//                    while ((foundIndex = (" " + fileText + " ").indexOf(" " + word + " ", offset)) > -1) {
//                        count++;
//                        offset = foundIndex + 1;
//                    }
//                    features += ((count == 0) ? "\"0\"" : "\"1\"") + " ";
//                }
//                datFile.setFeatures(features);
//                datFile.setLength(fileLength);
//
//                String categoriesBasis = "";
//
//                for (String element : categories) {
//                    String categoryMin = categoryName
//                            .replaceAll("[^A-Za-z_]+", "_")
//                            .replaceAll("__+", "_");
//
//                    categoriesBasis += ((element.contains(categoryMin)) ? "\"1\"" : "\"0\"") + " ";
//                }
//                categoriesBasis += categoryName;
//                datFile.setCategoryBasis(categoriesBasis);
//
//                boolean isUnknown = isUnknown(features);
//                datFile.setUnknown(isUnknown); // isUnknown = false
//                datFileList.add(datFile);
//            }
//            dao.batchSave(datFileList);
//        }
//        System.out.println("Done with: " + categoryName);
//    }
//
//    private String stemmString(String fileText) throws Exception {
//        return StemmerService.stemmString(fileText);
//    }
//
//    private boolean isUnknown(String features) {
//        String patternToCompare = "";
//        for (Vector<String> dictionaryWord : dictionaryWords) {
//            for (String ignored : dictionaryWord) {
//                patternToCompare += "\"0\"" + " ";
//            }
//        }
////        patternToCompare = patternToCompare.trim();
//        return features.equals(patternToCompare);
//    }
//}
//
