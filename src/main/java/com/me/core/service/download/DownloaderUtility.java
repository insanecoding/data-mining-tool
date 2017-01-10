package com.me.core.service.download;

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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DownloaderUtility extends StoppableObservable {

    @NonNull
    @Getter
    private ExecutorService executorService;

    private final MyDao dao;

    @Autowired
    public DownloaderUtility(MyDao dao, @Qualifier("progressWatcher") ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.dao = dao;
    }

    @Data
    @AllArgsConstructor
    // inner class
    private class Downloads {
        private Queue<HTML> htmls;
        private Queue<Connect> connects;
    }

    void processSingleCategory(DownloaderParameters parameters,
                               Category category) throws InterruptedException {
        super.updateMessageCheck("downloading HTMLs for category: " + category.getCategoryName());

        List<Website> websites = dao.findWebsitesByCategory(category);
        Downloads downloads = downloadHTMLsFor(websites, parameters);

        Queue<HTML> HTMLs = downloads.getHtmls();
        dao.batchSave(HTMLs);
        Queue<Connect> connects = downloads.getConnects();
        dao.batchSave(connects);

        super.updateMessage(">>>>> HTMLs actually saved: " + HTMLs.size() +
                " ( " + category.getCategoryName() + " )");

        super.updateMessageCheck("finished with category: " + category.getCategoryName());
    }

    private Downloads downloadHTMLsFor(List<Website> input, DownloaderParameters parameters)
            throws InterruptedException {
        int threadsNumber = parameters.getThreadsNumber();
        int connectTimeOut = parameters.getConnectTimeOut();
        int readTimeOut = parameters.getReadTimeOut();
        int websitesNum = parameters.getDownloadsPerCategory();
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
        executorService = executor;
        // input links are in this queue
        Queue<Website> in = new ConcurrentLinkedQueue<>(input);
        // results will be here
        Queue<HTML> htmls = new ConcurrentLinkedQueue<>();
        // connection status will be here
        Queue<Connect> connects = new ConcurrentLinkedQueue<>();
        // assign tasks
        in.forEach(website -> executor.execute(new SingleThreadDownloader(in, htmls, connects,
                connectTimeOut, readTimeOut)));
        // close service after execution completed
        closeExecutor(websitesNum, executor, htmls, connects);

        return new Downloads(htmls, connects);
    }


    void shutDownExecutor() {
        this.executorService.shutdownNow();
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
}
