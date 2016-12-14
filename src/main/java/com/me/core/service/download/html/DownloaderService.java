package com.me.core.service.download.html;

import com.me.common.Executable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.Connect;
import com.me.core.domain.entities.HTML;
import com.me.core.domain.entities.Website;
import com.me.core.service.dao.MyDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DownloaderService extends StoppableObservable implements Executable {

    @NonNull
    @Value("#{T(java.util.Arrays).asList('${app.categories}')}")
    private List<String> categories;

    @Value("${app.downloadsPerCategory}")
    private int downloadsPerCategory;

    @Value("${app.thread.number}")
    private int threadsNumber;

    @Value("${app.read.timeout}")
    private int readTimeOut;

    @Value("${app.connect.timeout}")
    private int connectTimeOut;

    private final MyDao dao;

    @NonNull
    @Getter
    private ExecutorService executorService;

    @Data
    @AllArgsConstructor
    private class Downloads {
        private Queue<HTML> htmls;
        private Queue<Connect> connects;
    }

    @Autowired
    public DownloaderService(@Qualifier("myDaoImpl") MyDao dao,
                      @Qualifier("progressWatcher") ProgressWatcher watcher) {
        this.dao = dao;
        super.addSubscriber(watcher);
    }

    @Override
    public void execute(Object... args) throws Exception {
        Collections.sort(categories);
        downloadHTMLs(categories, downloadsPerCategory);
    }

    private void downloadHTMLs(final List<String> desiredCategories,
                               final int downloadPerCategory) throws Exception {
        // make sure the categories are sorted alphabetically
        Collections.sort(desiredCategories);
        List<Category> categories = dao.findCategoriesByNames(desiredCategories);
        super.updateMeta("starting download task for categories");

        for (Category category : categories) {
            processOneCategory(downloadPerCategory, categories, category);
        }
    }

    private void processOneCategory(int downloadPerCategory, List<Category> categories,
                                    Category category) throws InterruptedException {
        super.updateWorkingCheck("processing category: " + category.getCategoryName(),
                categories.indexOf(category), categories.size());

        List<Website> websites = dao.findWebsitesByCategory(category);
        Downloads downloads = downloadHTMLsFor(websites, downloadPerCategory);

        Queue<HTML> HTMLs = downloads.getHtmls();
        dao.batchSave(HTMLs);
        Queue<Connect> connects = downloads.getConnects();
        dao.batchSave(connects);

        super.updateMeta(">>>>> HTMLs actually saved: " + HTMLs.size() +
                " ( " + category.getCategoryName() + " )");

        super.updateWorkingCheck("finished with category: " + category.getCategoryName(),
                categories.indexOf(category) + 1, categories.size());
    }

    private Downloads downloadHTMLsFor(List<Website> input, int websitesNum)
            throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
        executorService = executor;
        // input links are in this queue
        Queue<Website> in = new ConcurrentLinkedQueue<>(input);
        // results will be here
        Queue<HTML> htmls = new ConcurrentLinkedQueue<>();
        // connection status will be here
        Queue<Connect> connects = new ConcurrentLinkedQueue<>();
        // assign tasks
        in.forEach(website -> executor.execute(new Downloader(in, htmls, connects,
                connectTimeOut, readTimeOut)));
        // close service after execution completed
        closeExecutor(websitesNum, executor, htmls, connects);

        return new Downloads(htmls, connects);
    }

    private void closeExecutor(int websitesNum, ExecutorService executorService,
                               Queue<HTML> out, Queue<Connect> connects) throws InterruptedException {
        // do not submit any other tasks
        executorService.shutdown();

        // Wait until all threads are finished
        while (!executorService.isTerminated()) {
            if (out.size() >= websitesNum && connects.size() >= websitesNum) {
                executorService.shutdownNow();
                super.checkCancel();
            }
            else {
                super.checkCancel();
//                logger.info("current size: {}", out.size());
            }
        }
    }

    @Override
    public void beforeCancel(){
        executorService.shutdownNow();
    }

    @Override
    public void afterCancel() {
    }
}
