package com.me.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AsyncExecutor extends StoppableObservable {

    private Thread thread;
    private final Runner runner;
    private final ProgressWatcher watcher;
    private boolean wasStopped = false;

    @Autowired
    public AsyncExecutor(@Qualifier("runner") Runner runner,
                         @Qualifier("progressWatcher") ProgressWatcher watcher) {
        this.runner = runner;
        this.watcher = watcher;
        super.addSubscriber(watcher);
    }

    private void invoke(MyExecutable service, boolean enableAsync) {
        this.watcher.reset();
        this.runner.setService(service);
        this.thread = new Thread(runner);
        this.thread.start();

        if (!enableAsync) {
            waitComplete();
        }
    }

    public void invokeAll(List<MyExecutable> executables) {
        this.wasStopped = false;
        executables.forEach(executable -> {
            if (!this.wasStopped) {
                double currentStep = executables.indexOf(executable);
                double totalSteps = executables.size();
                super.updateMessage(executable.getName(), currentStep, totalSteps);
                invoke(executable, false);
                super.updateMessage( ++currentStep, totalSteps);
            }
        });
    }

    public void stop() {
        this.wasStopped = true;
        this.runner.cleanUp();
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
