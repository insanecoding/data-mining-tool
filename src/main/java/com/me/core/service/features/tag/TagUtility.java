//package com.me.core.service.features.tag;
//
//import org.apache.commons.collections4.CollectionUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class TagUtility {
//
//    private List<String> tagsToSkip;
//
//    public List<String> getTagsToSkip() {
//        return tagsToSkip;
//    }
//
//    public void setTagsToSkip(List<String> tagsToSkip) {
//        this.tagsToSkip = tagsToSkip;
//    }
//
//    public int countAllTags(String html){
//        return streamToList(html).size();
//    }
//
//    public int countTagOccur(List<String> tagList, String tag){
//        return Collections.frequency(tagList, tag);
//    }
//
//    public List<String> getUniqueTagsInPage(String html){
//        List<String> uniques =
//                htmlTagsAsStream(html)
//                .distinct()
//                .collect(Collectors.toList());
//        return excludeTagsToSkip(uniques);
//    }
//
//    public List<String> getAllTagsInPage(String html) {
//        List <String> allTags = streamToList(html);
//        return excludeTagsToSkip(allTags);
//    }
//
//    /**
//     * Transforms list to exclude <code>tagsToSlip</code> from it
//     */
//    private List<String> excludeTagsToSkip(List <String> tags){
//        Collection<String> col =
//                CollectionUtils.subtract(tags, tagsToSkip);
//        return new ArrayList<>(col);
//    }
//
//    /**
//     * utility method: converts Stream to List
//     * list contains tag duplicates and tags that should be skipped
//     */
//    private List<String> streamToList(String html) {
//        return htmlTagsAsStream(html).collect(Collectors.toList());
//    }
//
//    /**
//     * utility method: uses JSoup, extracts all HTML tags from page
//     * ignores <code>#root</code> element, created by JSoup
//     * and puts all other html tags to Stream
//     */
//    private Stream<String> htmlTagsAsStream(String html){
//        // use JSoup to parse html
//        Document doc = Jsoup.parse(html);
//         /*list of all html tags from page (they have duplicates!)
//         and not include <code>#root</code> element, which is used by JSoup and is not an html-tag*/
//        return doc.getAllElements()
//                .stream()
//                .map(e -> e.tagName().toLowerCase())
//                .filter(elem -> !elem.equals("#root"));
//    }
//}
