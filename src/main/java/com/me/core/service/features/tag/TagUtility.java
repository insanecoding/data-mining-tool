package com.me.core.service.features.tag;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class TagUtility {

    int countAllTags(String html){
        return streamToList(html).size();
    }

    int countTagOccur(List<String> tagList, String tag){
        return Collections.frequency(tagList, tag);
    }

    List<String> getUniqueTagsInPage(String html, List<String> tagsToSkip){
        Optional<Stream<String>> optional = htmlTagsAsStream(html);
        List<String> result = new ArrayList<>();
        if (optional.isPresent()) {
            result = optional.get().distinct().collect(Collectors.toList());
        }
        return excludeTagsToSkip(result, tagsToSkip);
    }

    List<String> getAllTagsInPage(String html, List<String> tagsToSkip) {
        List <String> allTags = streamToList(html);
        return excludeTagsToSkip(allTags, tagsToSkip);
    }

    /**
     * Transforms list to exclude <code>tagsToSkip</code> from it
     */
    private List<String> excludeTagsToSkip(List<String> allTags, List<String> tagsToSkip){
        Collection<String> col =
                CollectionUtils.subtract(allTags, tagsToSkip);
        return new ArrayList<>(col);
    }

    /**
     * utility method: converts Stream to List
     * list contains tag duplicates and tags that should be skipped
     */
    private List<String> streamToList(String html) {
        Optional<Stream<String>> optional = htmlTagsAsStream(html);
        List<String> result = new ArrayList<>();
        if (optional.isPresent()) {
            result = optional.get().collect(Collectors.toList());
        }
        return result;
    }

    /**
     * utility method: uses JSoup, extracts all HTML tags from page
     * ignores <code>#root</code> element, created by JSoup
     * and puts all other html tags to Stream
     */
    private Optional<Stream<String>> htmlTagsAsStream(String html){
        Stream<String> result = null;
        try {
            if (!html.matches("(^$|\\s+)")) {
                // use JSoup to parse html
                Document doc = Jsoup.parse(html);
         /*list of all html tags from page (they have duplicates!)
         and not include <code>#root</code> element, which is used by JSoup and is not an html-tag*/
                result = doc.getAllElements()
                        .stream()
                        .map(e -> e.tagName().toLowerCase())
                        .filter(elem -> !elem.equals("#root"));
            }
        } catch (IllegalArgumentException iae) {
            log.warn("illegal argument: not HTML");
        }
        return Optional.ofNullable(result);
    }
}
