package com.me.core.service.download;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.Category;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class DownloaderService extends StoppableObservable implements MyExecutable {

    @NonNull
    private List<String> categories;

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
        Collections.sort(categories);

        List<Category> categoryObjects = dao.findCategoriesByNames(categories);
        super.checkCancel();

        for (Category category : categoryObjects) {
            downloaderUtility.processSingleCategory(parameters, category);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Map<String, Object> param) {
        Map<String, Object> downloaderProperties = (Map<String, Object>) param.get("downloader");
        this.categories = (List<String>) downloaderProperties.get("categories");
        int downloadsPerCategory = (int) downloaderProperties.get("downloadsPerCategory");
        int threadsNumber = (int) downloaderProperties.get("threadsNumber");
        int readTimeOut = (int) downloaderProperties.get("readTimeout");
        int connectTimeOut = (int) downloaderProperties.get("connectTimeout");
        this.parameters = new DownloaderParameters(downloadsPerCategory, threadsNumber,
                readTimeOut, connectTimeOut);
    }

    @Override
    public void cleanUp(){
        downloaderUtility.shutDownExecutor();
        this.categories.clear();
    }


    @Override
    public String getName() {
        return "HTML Downloader";
    }
}
