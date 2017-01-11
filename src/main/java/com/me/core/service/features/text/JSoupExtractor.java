package com.me.core.service.features.text;

import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.HTML;
import com.me.core.domain.entities.Tag;
import com.me.core.domain.entities.TextFromTag;
import com.me.core.domain.entities.TextMain;
import com.me.core.service.langDetection.LangDetector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JSoupExtractor extends StoppableObservable implements TextExtractor {

    @Getter
    @Setter
    private LangDetector langDetector;

    @Autowired
    public JSoupExtractor(LangDetector langDetector,
                          @Qualifier("progressWatcher") ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.langDetector = langDetector;
    }

    @Override
    public Optional<TextMain> extractTextMain(HTML html) {

        String htmlContents = html.getHtml();
        TextMain textMain = null;

        if (!htmlContents.matches("(^$|\\s+)")) {
            String text = jSoupGetTextMain(htmlContents);
            int textLength = text.length();
            String lang = langDetector.detectLang(text);

            if (!text.equals("")) {
                textMain = new TextMain(html.getWebsite(), text, lang, textLength);
            }
        }

        return Optional.ofNullable(textMain);
    }


    @Override
    public List<TextMain> extractTextMain(List<HTML> htmls) throws InterruptedException {
        langDetector.setShortText(false);
        List<TextMain> textsMain = new LinkedList<>();

        for (HTML html : htmls) {
            extractTextMain(html).ifPresent(textsMain::add);
            super.checkCancel();
        }

        return textsMain;
    }

    /**
     * uses JSoup
     * returns from html string with text or empty string
     */
    private String jSoupGetTextMain(String htmlContents) {
        String text = "";

        try {
            Document doc = Jsoup.parse(htmlContents);
            Optional<Element> bodyElement = Optional.ofNullable(doc.body());


            if (bodyElement.isPresent()) {
                // get initial text from HTML
                String textInPage = bodyElement.get().text();
                text = cleanString(textInPage);

            }
        } catch (IllegalArgumentException e) {
            log.warn("illegal argument: not HTML!");
        }
        return text;
    }

    private String cleanString(String textInPage) {
        // leave only a...z, A...Z, 0...9 letters and whitespaces
        String text = textInPage.replaceAll("[^A-Za-z0-9 ]", " ").toLowerCase();
        // remove duplicate whitespaces
        text = text.replaceAll("\\s+", " ");
        // some more cleanup for whitespaces
        text = text.trim();
        return text;
    }


    @Override
    public Optional<TextFromTag> extractTextFromTag(HTML htmlContents, Tag tag) {
        String tagName = tag.getTagName();
        String html = htmlContents.getHtml();
        // extract text from this tag and create textFromTag object
        String result = extractTextFromTag(html, tagName);
        TextFromTag textFromTag = null;
        if (!result.equals("")) {
            int textLength = result.length();
            textFromTag = new TextFromTag(htmlContents.getWebsite(), tag, result, textLength);
        }
        return Optional.ofNullable(textFromTag);
    }

    private String extractTextFromTag(String html, String tagName) {
        String result = jSoupExtractFromTag(html, tagName);
        result = cleanString(result);
        return result;
    }

    private String jSoupExtractFromTag(String html, String tagName) {
        Document doc = Jsoup.parse(html);
        final String[] result = {""};

        switch (tagName) {
            case "title": {
                result[0] = doc.title();
                break;
            }
            case "meta:description": {
                processMetaTags(doc, result, tagName);
                break;
            }
            case "meta:keywords": {
                processMetaTags(doc, result, tagName);
                break;
            }
            case "h1":
            case "h2":
            case "h3":
            case "b":
            case "u":
                process_h1_h2_h3_b_u(tagName, doc, result);
                break;
            case "a":
                processLinks(doc, result, tagName);
                break;
            case "img":
                processImages(doc, result, tagName);
                break;
        }

        return result[0];
    }

    private void processMetaTags(Document doc, String[] result, String tagName) {
        // e.g: get "description" from "meta:description"
        String attributeName = tagName.split(":")[1];

        doc.select("meta").forEach(element -> {
            if (element.attr("name").compareToIgnoreCase(attributeName) == 0 &&
                    !element.attr("content").equals("")) {
                result[0] += " " + element.attr("content");
            }
        });
    }

    private void processImages(Document doc, String[] result, String tagName) {
        doc.select(tagName).forEach(element -> {
            // case 1: <img> tag - has alt-attribute
            if (element.tagName().equals("img") && element.hasAttr("alt")) {
                String value = element.attr("alt");
                result[0] += " " + value;
            }
        });
    }

    private void process_h1_h2_h3_b_u(String tagName, Document doc, String[] result) {
        doc.select(tagName).forEach(element -> {
            if (element.hasText()) {
                result[0] += " " + element.text();
            }
        });
    }

    private void processLinks(Document doc, String[] result, String tagName) {
        doc.select(tagName).forEach(element -> {
            // if has title attribute
            if (element.hasAttr("title")) {
                String value = element.attr("title");
                result[0] += " " + value;
            }
            // for text between <a> </a>
            if (element.hasText()) {
                String value = element.text();
                result[0] += " " + value;
            }
        });
    }
}
