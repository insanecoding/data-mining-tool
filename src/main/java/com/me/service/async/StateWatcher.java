//package com.me.service.async;
//
//import com.me.domain.State;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.ScheduledFuture;
//
//@Component
//public class StateWatcher {
//
//    private TaskScheduler taskScheduler =  new DefaultManagedTaskScheduler();
//    private ScheduledFuture sf;
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private SimpMessageSendingOperations simpMessageSendingOperations;
//    private final ProgressWatcher watcher;
//
//
//    @Autowired
//    public StateWatcher(@Qualifier("progressWatcher") ProgressWatcher watcher,
//                        SimpMessageSendingOperations simpMessageSendingOperations) {
//        this.simpMessageSendingOperations = simpMessageSendingOperations;
//        this.watcher = watcher;
//    }
//
//
//    public void startMonitoring() {
//
//         sf = taskScheduler.scheduleWithFixedDelay(() -> {
//            try {
//                // send reply to the broker
//                State state = watcher.getState();
//                logger.info(" >>> [websocket:changed] state now: {}", state);
//                simpMessageSendingOperations.convertAndSend("/topic/broker", state);
//            } catch (Exception e) {
//                logger.error("exception occurred: {}", e);
//            }
//
//        }, 500);
//    }
//
//    public void cancelMonitoring(){
//        sf.cancel(true);
//    }
//}
