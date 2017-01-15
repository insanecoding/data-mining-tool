//package com.me.core.service.experiment.text.aml;
//
//import com.me.data.dao.WebsiteDAO;
//import com.me.data.entities.*;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Vector;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class AMLDATUtility {
//
//    private WebsiteDAO websiteDAO;
//    private Vector<String>[] dictionaryWords;
//    private Vector<String> categories = new Vector();
//
//    public WebsiteDAO getWebsiteDAO() {
//        return websiteDAO;
//    }
//
//    public void setWebsiteDAO(WebsiteDAO websiteDAO) {
//        this.websiteDAO = websiteDAO;
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
//                            + m.group(2).replaceAll("[^A-Za-z&_]+", "_").replaceAll("__+", "_");
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
//    private String stemmString(String str) throws Exception {
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
//                websiteDAO.findDictionaryWords(experiment);
//
//        validateDictionaryWords(count, dictionary, wordsFromDictionary);
//
//        List<AmlFile> amlFiles = new LinkedList<>();
//        for (int i = 0; i < count.length; i++) {
//            for (String word : dictionary[i]) {
//                AmlFile amlFile = new AmlFile();
//                amlFile.setExperiment(experiment);
//                amlFile.setFeature(experiment.getExpName() + '_' + word);
//                amlFiles.add(amlFile);
//            }
//        }
//        websiteDAO.batchSave(amlFiles);
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
//            for (int i = 0; i < dictionaryWords.length; i++) {
//                DatFile datFile = new DatFile();
//                datFile.setExperiment(experiment);
//                datFile.setWebsite(text.getWebsite());
//
//                String features = "";
//                for (String word : dictionaryWords[i]) {
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
////                    features += ((category.indexOf(categoryMin) > -1) ? "\"1\"" : "\"0\"") + " ";
//                    categoriesBasis += ((element.indexOf(categoryMin) > -1) ? "\"1\"" : "\"0\"") + " ";
//                }
//                categoriesBasis += categoryName;
//                datFile.setCategoryBasis(categoriesBasis);
////                features += categoryName;
//
//                boolean isUnknown = isUnknown(features);
//                datFile.setUnknown(isUnknown); // isUnknown = false
//                datFileList.add(datFile);
//            }
//            websiteDAO.batchSave(datFileList);
//        }
//        System.out.println("Done with: " + categoryName);
//    }
//
//    private boolean isUnknown(String features) {
//        String patternToCompare = "";
//        for (int i = 0; i < dictionaryWords.length; i++) {
//            for (String ignored : dictionaryWords[i]) {
//                patternToCompare += "\"0\"" + " ";
//            }
//        }
////        patternToCompare = patternToCompare.trim();
//        return features.equals(patternToCompare);
//    }
//}
//
