package com.me.core.service.features.text;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.HTML;
import com.me.core.domain.entities.Tag;
import com.me.core.domain.entities.TextFromTag;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TextFromTagExtractorService extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private List<String> categories;
    @Getter @Setter
    private List<String> tags;
    @Getter @Setter
    private TextExtractor extractor;
    @Getter @Setter
    private MyDao dao;

    @Autowired
    public TextFromTagExtractorService(MyDao dao, TextExtractor extractor,
                                       @Qualifier("progressWatcher") ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.dao = dao;
        this.extractor = extractor;
    }

    @Override
    public void execute() throws Exception {
        List<Category> categoryObjects = dao.findCategoriesByNames(categories);
        categoryObjects.sort(Comparator.comparing(Category::getCategoryName));
        List<Tag> tagObjects = createTagObjects();
        tagObjects.sort(Comparator.comparing(Tag::getTagName));

        for (Category category: categoryObjects) {
            for(Tag tag: tagObjects) {
                super.updateMessageCheck("extracting text from tag: " + tag.getTagName() +
                        " for category: " + category.getCategoryName());

                List<HTML> notProcessed = getNotProcessed(category, tag);
                List<TextFromTag> result = extractor.extractTextFromTag(notProcessed, tag);

                dao.batchSave(result);
                result.clear();
            }
        }
    }

    private List<Tag> createTagObjects() {
        List<Tag> tagObjects = dao.findTagsByNames(tags);

        if (tagObjects.size() == 0) {
            tagObjects = tags.stream().map(Tag::new)
                    .map(tag -> dao.trySaveTag(tag))
                    .collect(Collectors.toList());
        }
        return tagObjects;
    }

    private List<HTML> getNotProcessed(Category category, Tag tag) {
        List<HTML> HTMLs = dao.findByCategory("htmls", category);
        List<Long> alreadyProcessed =
                dao.alreadyProcessedIDsFor("texts_from_tags", category, tag);

        List<HTML> notProcessed = HTMLs.stream()
                .filter(html -> !alreadyProcessed.contains(html.getWebsite().getWebsiteId()))
                .collect(Collectors.toList());
        alreadyProcessed.clear();
        HTMLs.clear();
        return notProcessed;
    }

    @Override
    public String getName() {
        return "Text from tag extractor";
    }
}
