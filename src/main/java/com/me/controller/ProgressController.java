package com.me.controller;

import com.me.service.async.AsyncExecutor;
import com.me.service.async.ProgressWatcher;
import com.me.service.exampleservice.MockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ScheduledFuture;

@Controller
public class ProgressController {

    private final AsyncExecutor executor;
    private final ProgressWatcher watcher;
    private final MockService service;

    @Autowired
    public ProgressController(@Qualifier("progressWatcher")ProgressWatcher watcher,
                              AsyncExecutor executor,
                              @Qualifier("mockService") MockService service) {
        this.watcher = watcher;
        this.executor = executor;
        this.service = service;
    }

    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
    public void invoker() throws InterruptedException {
        TaskScheduler taskScheduler = new DefaultManagedTaskScheduler();
        ScheduledFuture sf = taskScheduler.scheduleWithFixedDelay(() -> {
            try {
                watcher.sendState();
            } catch (Exception ignored) {
            }

        }, 500);
        executor.invoke(service, false);
        Thread.sleep(1000);
        sf.cancel(true);
    }
}
