//package com.me.core.service.langDetection;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.tika.langdetect.OptimaizeLangDetector;
//import org.apache.tika.language.detect.LanguageDetector;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//
//@Slf4j
//public class TikaLangDetector implements LangDetector {
//
//    private LanguageDetector langDetector;
//
//    public TikaLangDetector(boolean isShortText) throws IOException {
//
//        langDetector = new OptimaizeLangDetector().loadModels();
//        if (isShortText) {
//            langDetector.setShortText(true);
//        }
//    }
//
//    public boolean isShortText() {
//        return langDetector.isShortText();
//    }
//
//    public void setShortText(boolean isShortText) {
//        langDetector.setShortText(isShortText);
//    }
//
//    @Override
//    public String detectLang(String text) {
//        return langDetector.detect(text).getLanguage();
//    }
//}
