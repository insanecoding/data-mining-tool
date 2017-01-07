package com.me.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * executes service asynchronously
 * can be interrupted
 */
@Component
public class Runner implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * service to execute
     */
    @NonNull @Getter @Setter
    private MyExecutable service;

    @Override
    public void run() {
        try {
            logger.info("starting async execution of {}", service.getClass().getName());
            service.execute();
            service.cleanUp();
            logger.info("async execution finished");
        } catch (Exception e) {
            MyExceptionHandler.handleExceptions(e);
        }
    }

    public void cleanUp(){
        service.cleanUp();
    }
}
