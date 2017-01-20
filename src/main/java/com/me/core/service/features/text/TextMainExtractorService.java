package com.me.core.service.features.text;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.HTML;
import com.me.core.domain.entities.TextMain;
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
public class TextMainExtractorService extends StoppableObservable implements MyExecutable{

    @Getter @Setter
    private List<String> categories;
    @Getter @Setter
    private boolean useAllRelevantCategories;

    private final TextExtractor extractor;
    private final MyDao dao;

    @Autowired
    public TextMainExtractorService(TextExtractor extractor, MyDao dao,
                                    @Qualifier("progressWatcher") ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.extractor = extractor;
        this.dao = dao;
    }

    @Override
    public void execute() throws Exception {

        if (useAllRelevantCategories)
            categories = dao.findRelevantCategoriesByHTML();

        List<Category> categoryObjects = dao.findCategoriesByNames(categories);
        categoryObjects.sort(Comparator.comparing(Category::getCategoryName));

        for (Category category : categoryObjects) {
            super.updateMessageCheck("extracting text main for category: " + category.getCategoryName());

            List<HTML> notProcessed = getNotProcessed(category);
            List<TextMain> textsMain = extractor.extractTextMain(notProcessed);

            dao.batchSave(textsMain);
            textsMain.clear();
        }
    }

    private List<HTML> getNotProcessed(Category category) {
        List<HTML> HTMLs = dao.findByCategory("htmls", category);
        List<Long> alreadyProcessed =
                dao.alreadyProcessedIDsFor("texts_main", category);

        List<HTML> notProcessed = HTMLs.stream()
                .filter(html -> !alreadyProcessed.contains(html.getWebsite().getWebsiteId()))
                .collect(Collectors.toList());
        alreadyProcessed.clear();
        HTMLs.clear();
        return notProcessed;
    }

    @Override
    public String getName() {
        return "Main text extractor";
    }
}
