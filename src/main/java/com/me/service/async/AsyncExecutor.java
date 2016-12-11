package com.me.service.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AsyncExecutor {

    private Thread thread;
    private final Runner runner;
    private final ProgressWatcher watcher;

    @Autowired
    public AsyncExecutor(@Qualifier("runner") Runner runner,
                         @Qualifier("progressWatcher") ProgressWatcher watcher) {
        this.runner = runner;
        this.watcher = watcher;
    }

    public void invoke(MyExecutable service, boolean enableAsync) {
        this.watcher.reset();
        this.runner.setService(service);
        this.thread = new Thread(runner);
        this.thread.start();

        if (!enableAsync) {
            waitComplete();
        }
    }

    public void stop() {
        this.thread.interrupt();
        this.thread = null;
        this.watcher.reset();
    }

    private void waitComplete() {
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            MyExceptionHandler.handleExceptions(e);
        }
    }
}
