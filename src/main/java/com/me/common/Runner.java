package com.me.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * executes service asynchronously
 * can be interrupted
 */
@Component
@Slf4j
public class Runner implements Runnable {
    /**
     * service to execute
     */
    @NonNull @Getter @Setter
    private MyExecutable service;

    @Override
    public void run() {
        try {
            String serviceName = service.getClass().getName();
            log.info("starting async execution of {}", serviceName);
            service.execute();
            cleanUp();
            log.info("async execution finished");
        } catch (Exception e) {
            MyExceptionHandler.handleExceptions(e);
        }
    }

    public void cleanUp(){
        String serviceName = service.getClass().getName();
        log.info("cleaning up for {}", serviceName);
        service.cleanUp();
    }
}
