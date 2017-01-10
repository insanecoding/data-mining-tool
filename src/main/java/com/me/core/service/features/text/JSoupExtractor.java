//package com.me.core.service.features.text;
//
//import com.me.data.entities.HTML;
//import com.me.data.entities.Tag;
//import com.me.data.entities.TextFromTag;
//import com.me.data.entities.TextMain;
//import com.me.services.langDetection.LangDetector;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//
//import java.util.Optional;
//
//// todo: create factory for all the existing entities
//// // TODO: 12.09.2016 check the existing code after entities modification
//// // TODO: 12.09.2016 replace long switch-case using patterns!
//public class JSoupExtractor implements TextExtractor {
//
//    private LangDetector langDetector;
//
//    public LangDetector getLangDetector() {
//        return langDetector;
//    }
//
//    public void setLangDetector(LangDetector langDetector) {
//        this.langDetector = langDetector;
//    }
//
//    /**
//     * uses JSoup
//     * returns from html string with text or empty string
//     */
//    @NotNull
//    public String jSoupGetTextMain(String htmlContents) {
//        Document doc = Jsoup.parse(htmlContents);
//        Optional<Element> bodyElement = Optional.ofNullable(doc.body());
//        String text = "";
//
//        if (bodyElement.isPresent()) {
//            // get initial text from HTML
//            String textInPage = bodyElement.get().text();
//            text = cleanString(textInPage);
//
//        }
//        return text;
//    }
//
//    @NotNull
//    private String cleanString(String textInPage) {
//         // leave only a...z, A...Z, 0...9 letters and whitespaces
//        String text = textInPage.replaceAll("[^A-Za-z0-9 ]", " ").toLowerCase();
//        // remove duplicate whitespaces
//        text  = text.replaceAll("\\s+", " ");
//        // some more cleanup from whitespaces
//        text = text.trim();
//        return text;
//    }
//
//    @Override
//    public Optional<TextMain> extractTextMain(HTML html) {
//        String htmlContents = html.getHtml();
//        TextMain textMain = null;
//
//        String text = jSoupGetTextMain(htmlContents);
//
//        int textLength = text.length();
//        String lang = langDetector.detectLang(text);
//
//        if (!text.equals("")) {
//            textMain = new TextMain();
//            textMain.setWebsite(html.getWebsite());
//            textMain.setLang(lang);
//            textMain.setLength(textLength);
//            textMain.setText(text);
//        }
//
//        return Optional.ofNullable(textMain);
//    }
//
//    @Override
//    public Optional<TextFromTag> extractTextFromTag(HTML htmlContents, Tag tag) {
//        String tagName = tag.getTagName();
//        String html = htmlContents.getHtml();
//        // extract text from this tag and create textFromTag object
//        String result = extractTextFromTag(html, tagName);
//        TextFromTag textFromTag = null;
//        if (!result.equals("")) {
//            textFromTag = new TextFromTag();
//            int textLength = result.length();
//            textFromTag.setLength(textLength);
//            textFromTag.setWebsite(htmlContents.getWebsite());
//            textFromTag.setText(result);
//            textFromTag.setTag(tag);
//        }
//        return Optional.ofNullable(textFromTag);
//    }
//
//    public String extractTextFromTag(String html, String tagName) {
//        String result = jSoupExtractFromTag(html, tagName);
//        result = cleanString(result);
//        return result;
//    }
//
//    private String jSoupExtractFromTag(String html, String tagName) {
//        Document doc = Jsoup.parse(html);
//        final String[] result = {""};
//
//        if (tagName.equals("title")) {
//            result[0] = doc.title();
//        } else {
//            switch (tagName) {
//                case "meta:description":
//                    doc.select("meta").forEach(element -> {
//                        if (element.attr("name").compareToIgnoreCase("description") == 0 &&
//                                !element.attr("content").equals("")) {
//                            result[0] += " " + element.attr("content");
//                        }
//                    });
//                    break;
//                case "meta:keywords":
//                    doc.select("meta").forEach(element -> {
//                        if (element.attr("name").compareToIgnoreCase("keywords") == 0 &&
//                                !element.attr("content").equals("")) {
//                            result[0] += " " + element.attr("content");
//                        }
//                    });
//                    break;
//                case "h1":
//                case "h2":
//                case "h3":
//                case "b":
//                case "u":
//                    doc.select(tagName).forEach(element -> {
//                        if (element.hasText()) {
//                            result[0] += " " + element.text();
//                        }
//                    });
//                    break;
//                case "a":
//                    doc.select(tagName).forEach(element -> {
//                        // if has title-attribute
//                        if (element.hasAttr("title")) {
//                            String value = element.attr("title");
//                            result[0] += " " + value;
//                        }
//                        // for text between <a> </a>
//                        if (element.hasText()) {
//                            String value = element.text();
//                            result[0] += " " + value;
//                        }
//                    });
//                    break;
//                case "img":
//                    doc.select(tagName).forEach(element -> {
//                        // case 1: <img> tag - has alt-attribute
//                        if (element.tagName().equals("img") && element.hasAttr("alt")) {
//                            String value = element.attr("alt");
//                            result[0] += " " + value;
//                        }
//                    });
//                    break;
//            }
//        }
//        return result[0];
//    }
//}
