package com.me.core.service.features.nGrams;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.NGrams;
import com.me.core.domain.entities.Website;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NGramExtractorService extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private int maxNGramSize;
    @Getter @Setter
    private List<String> categories;
    @Getter @Setter
    private boolean useAllRelevantCategories;

    private MyDao dao;
    private NGramCreator nGramCreator;

    @Autowired
    public NGramExtractorService(MyDao dao, NGramCreator nGramCreator,
                                 @Qualifier("progressWatcher") ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.dao = dao;
        this.nGramCreator = nGramCreator;
    }

    @Override
    public void execute() throws Exception {

        if (useAllRelevantCategories)
            categories = dao.findRelevantCategoriesByHTML();

        List<Category> categoryObjects = dao.findCategoriesByNames(categories);
        categoryObjects.sort(Comparator.comparing(Category::getCategoryName));

        for (Category category : categoryObjects) {
            for (int nGramSize = 2; nGramSize <= maxNGramSize; nGramSize++) {
                List<Website> notProcessed = getNotProcessed(category, nGramSize);
                super.updateMessageCheck("creating NGrams with N=" + nGramSize +
                        " for category: "+ category.getCategoryName());

                extractNGrams(nGramSize, notProcessed);
            }
        }
    }

    private void extractNGrams(int nGramSize, List<Website> websites) throws InterruptedException {
        List<NGrams> nGrams = new LinkedList<>();
        for (Website website : websites) {
            nGramCreator.generateNGrams(website, nGramSize).ifPresent(nGrams::add);
            super.checkCancel();
        }

        dao.batchSave(nGrams);
        nGrams.clear();
    }

    private List<Website> getNotProcessed(Category category, int nGramSize) {
        List<Website> websites = dao.findByCategory("websites", category);
        List<Long> alreadyProcessed = dao.alreadyProcessedIDsFor("ngrams", category, nGramSize);

        List<Website> notProcessed = websites.stream()
                .filter(website -> !alreadyProcessed.contains(website.getWebsiteId()))
                .collect(Collectors.toList());
        alreadyProcessed.clear();
        websites.clear();
        return notProcessed;
    }

    @Override
    public String getName() {
        return "NGram extractor";
    }
}
