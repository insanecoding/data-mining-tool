package com.me.core.service.download.html;

import com.me.core.domain.entities.Connect;
import com.me.core.domain.entities.HTML;
import com.me.core.domain.entities.Website;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Queue;

public class Downloader implements Runnable  {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Queue<Website> input;
    private Queue<HTML> htmls;
    private Queue<Connect> connects;
    private int connectTimeOut;
    private int readTimeOut;

    public Downloader(Queue<Website> input, Queue<HTML> htmls, Queue<Connect> connects,
               int connectTimeOut, int readTimeOut) {
        this.input = input;
        this.htmls = htmls;
        this.connects = connects;
        this.connectTimeOut = connectTimeOut;
        this.readTimeOut = readTimeOut;
    }

    @Data
    @AllArgsConstructor
    private class HtmlAndConnect {
        private HTML html;
        private Connect connect;
    }

    @Override
    public void run() {
        try {
            // try to get website from the queue
            StopWatch sw = new StopWatch(this.getClass().getName());
            Website website = input.poll();

            // check whether elem exists
            if (website != null) {
                // start measure time
                logger.info("downloading website {}", website.getUrl());
                processNonNullSite(sw, website);
            }
        } catch (InterruptedException e) {
            logger.warn("I was interrupted");
        }
    }

    private void processNonNullSite(StopWatch sw, Website website)
            throws InterruptedException {
        sw.start();
        HtmlAndConnect result = getHTML(website);
        HTML html = result.getHtml();
        Connect connect = result.getConnect();

        if (!html.getHtml().equals("")) {
            htmls.offer(html);
        } else {
            // if html is equal "", but status is not ERROR
            if (!connect.getResult().equals(DownloadResult.ERROR)) {
                connect = new Connect(website, 0, DownloadResult.EMPTY);
            }
        }
        sw.stop();
        long downloadTime = sw.getTotalTimeMillis();
        connect.setDuration(downloadTime);
        if (downloadTime > 15_000) {
            logger.info("### downloading {} took: {} ms", website.getUrl(), downloadTime);
        }
        connects.offer(connect);
    }

    private HtmlAndConnect getHTML(Website website) throws InterruptedException {
        HTML html;
        Connect connect;
        try {
            String htmlSource;
            HttpURLConnection urlConn = connectToUrl(website.getUrl());
            try (InputStreamReader is = new InputStreamReader(urlConn.getInputStream())) {
                // transform InputStream to String
                htmlSource = IOUtils.toString(is);
            }
        /* always replace nulls "\u0000" to avoid this SQL exception while inserting:
         "invalid byte sequence for encoding "UTF8": 0x00" */
            if (htmlSource.contains("\u0000")) {
                htmlSource = htmlSource.replaceAll("\u0000", "");
            }
            connect = new Connect(website, 0, DownloadResult.SUCCESS);
            html = new HTML(website, htmlSource);
        } catch (Exception e) {
            handleDownloadExceptions(e);
            connect = new Connect(website, 0, DownloadResult.ERROR);
            html = new HTML(website, "");
        }

        return new HtmlAndConnect(html, connect);
    }

    private void handleDownloadExceptions(Exception e) {
        if (!(e instanceof FileNotFoundException || e instanceof UnknownHostException ||
                e instanceof ConnectException || e instanceof SocketTimeoutException ||
                e instanceof IOException)) {
            logger.error("!exception while downloading occurred {} - {}",
                    e.getClass(), e.getMessage());
        }
    }

    private HttpURLConnection connectToUrl(String website) throws IOException {
        URL link = new URL(website);
        HttpURLConnection urlConn = (HttpURLConnection)link.openConnection();
        HttpURLConnection.setFollowRedirects(false);
        urlConn.setRequestMethod("GET");
        urlConn.setConnectTimeout(connectTimeOut);
        urlConn.setReadTimeout(readTimeOut);
        urlConn.setAllowUserInteraction(false);
        urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) " +
                "Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        return urlConn;
    }
}
