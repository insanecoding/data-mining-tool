package com.me.core.service.features.tag;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TagStatExtractService extends StoppableObservable implements MyExecutable {
    @Getter @Setter
    private List<String> categories;
    @Getter @Setter
    private List<String> tagsToSkip;
    @Getter @Setter
    private TagUtility tagUtility;
    @Getter @Setter
    private MyDao dao;

    @Autowired
    public TagStatExtractService(TagUtility tagUtility, MyDao dao,
                                 @Qualifier("progressWatcher") ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.tagUtility = tagUtility;
        this.dao = dao;
    }

    @Override
    public void execute() throws Exception {
        List<Category> categoryObjects = dao.findCategoriesByNames(this.categories);
        categoryObjects.sort(Comparator.comparing(Category::getCategoryName));
//        processTagCounts(categoryObjects);
        processTagsInPage(categoryObjects);
    }

    private void processTagCounts(List<Category> categoryObjects) throws InterruptedException {
        for (Category category: categoryObjects) {
            super.updateMessageCheck("counting tags for category: " + category.getCategoryName());
            List<HTML> notProcessedForTagCount = getNotProcessed(category, false);
            List<TagCount> tagCounts = createTagCountsList(notProcessedForTagCount);
            dao.batchSave(tagCounts);
            tagCounts.clear();
        }
    }

    private List<TagCount> createTagCountsList(List<HTML> htmls) throws InterruptedException {
        List<TagCount> counts = new LinkedList<>();
        // foreach html in category
        for (HTML html : htmls) {
            super.checkCancel();
            // count all tags in page
            int count = tagUtility.countAllTags(html.getHtml());
            // create respective object
            TagCount websiteTagCount = new TagCount(html.getWebsite(), count);
            counts.add(websiteTagCount);
        }
        return counts;
    }

    private void processTagsInPage(List<Category> categoryObjects) throws InterruptedException {
        for (Category category : categoryObjects) {
            super.updateMessageCheck("extracting tag statistics for category: " + category.getCategoryName());

            List<HTML> notProcessedForTagsInPage = getNotProcessed(category, true);
            extractTagsFromPages(notProcessedForTagsInPage);
        }
    }

    private void extractTagsFromPages(List<HTML> htmls) throws InterruptedException {
        // foreach html in category
        for (HTML html : htmls) {
            super.checkCancel();
            // create respective object
            List<TagsInPage> tagsInOnePage = processSingleHTML(html);
            dao.batchSave(tagsInOnePage);
        }
    }

    private List<TagsInPage> processSingleHTML(HTML html) throws InterruptedException {
        // list all tags from html
        List<String> tags = tagUtility.getAllTagsInPage(html.getHtml(), tagsToSkip);
        // now list only unique tags
        List<String> unique = tagUtility.getUniqueTagsInPage(html.getHtml(), tagsToSkip);

        List<TagsInPage> result = new LinkedList<>();
        // foreach unique tag in html
        for (String tagName : unique) {
            int occurNumber = tagUtility.countTagOccur(tags, tagName);
            super.checkCancel();
            // create objects
            Tag tag = new Tag(tagName);
            tag = dao.trySaveTag(tag);
            TagsInPage tagInPage = new TagsInPage(html.getWebsite(), tag, occurNumber);
            result.add(tagInPage);
        }
        return result;
    }


    private List<HTML> getNotProcessed(Category category, boolean isForTagsInPage) {
        List<HTML> HTMLs = dao.findByCategory("htmls", category);
        List<Long> alreadyProcessed;
        if (isForTagsInPage) {
            alreadyProcessed = dao.alreadyProcessedIDsFor("tags_in_page", category);
        } else {
            alreadyProcessed = dao.alreadyProcessedIDsFor("tag_count", category);
        }

        List<HTML> notProcessed = HTMLs.stream()
                .filter(html -> !alreadyProcessed.contains(html.getWebsite().getWebsiteId()))
                .collect(Collectors.toList());
        alreadyProcessed.clear();
        HTMLs.clear();
        return notProcessed;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Map<String, Object> param) {
        Map<String, Object> props = (Map<String, Object>) param.get("tagStatExtractor");
        List<String> tempCategories = (List<String>) props.get("categories");
        List<String> tempTags = (List<String>) props.get("tagsToSkip");
        this.categories = new ArrayList<>(tempCategories);
        this.tagsToSkip = new ArrayList<>(tempTags);
    }

    @Override
    public String getName() {
        return "Tag Statistics Extractor";
    }
}
