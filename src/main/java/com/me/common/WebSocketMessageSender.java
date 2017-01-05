package com.me.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class WebSocketMessageSender {

    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    public WebSocketMessageSender(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    public void sendMessage(Object msg) {
        simpMessageSendingOperations.convertAndSend("/topic/broker", msg);
    }

}
