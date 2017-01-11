package com.me.core.service.langDetection;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class TikaLangDetector implements LangDetector {

    private LanguageDetector langDetector;

    public TikaLangDetector() throws IOException {
        langDetector = new OptimaizeLangDetector().loadModels();
    }

    public boolean isShortText() {
        return langDetector.isShortText();
    }

    public void setShortText(boolean isShortText) {
        langDetector.setShortText(isShortText);
    }

    @Override
    public String detectLang(String text) {
        return langDetector.detect(text).getLanguage();
    }
}
