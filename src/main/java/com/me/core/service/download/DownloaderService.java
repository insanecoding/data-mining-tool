package com.me.core.service.download;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.dto.DownloaderParameters;
import com.me.core.domain.entities.Category;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class DownloaderService extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private List<String> categories;

    @Getter @Setter
    private boolean useAllCategoriesWithMore1000;

    @Getter @Setter
    private DownloaderParameters parameters;

    @Getter
    private final MyDao dao;

    private final DownloaderUtility downloaderUtility;

    @Autowired
    public DownloaderService(@Qualifier("myDaoImpl") MyDao dao,
                             @Qualifier("progressWatcher") ProgressWatcher watcher,
                             DownloaderUtility downloaderUtility) {
        this.dao = dao;
        super.addSubscriber(watcher);
        this.downloaderUtility = downloaderUtility;
    }

    @Override
    public void execute() throws Exception {

        if (useAllCategoriesWithMore1000) {
            categories = dao.findRelevantCategories();
        }
        List<Category> categoryObjects = dao.findCategoriesByNames(categories);

        categoryObjects.sort(Comparator.comparing(Category::getCategoryName));
        super.checkCancel();

        for (Category category : categoryObjects) {
            downloaderUtility.processSingleCategory(parameters, category);
        }
    }

    @Override
    public void cleanUp(){
        downloaderUtility.shutDownExecutor();
    }


    @Override
    public String getName() {
        return "HTML Downloader";
    }
}
