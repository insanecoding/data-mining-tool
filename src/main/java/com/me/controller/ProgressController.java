//package com.me.controller;
//
//import com.me.domain.DTO;
//import com.me.domain.State;
//import com.me.service.async.AsyncExecutor;
//import com.me.service.async.ProgressWatcher;
//import com.me.service.exampleservice.MockService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
//import org.springframework.stereotype.Controller;
//
//import java.util.concurrent.ScheduledFuture;
//
//@Controller
//public class ProgressController {
//
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    // when client sends something to socket/progress
//    @MessageMapping("/progress")
//    // send the answer to the broker
//    @SendTo("/topic/broker")
//    public void invoker(DTO dto) throws InterruptedException {
//        logger.info(" >>> [websocket] client connected");
//        logger.info(" >>> [websocket] the message was: {}", dto);
//
//
//
//    }
//}
