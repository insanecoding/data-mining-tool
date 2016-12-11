package com.me.service.async;

import com.me.domain.State;
import com.me.domain.States;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

@Component("progressWatcher")
public class ProgressWatcher implements Observer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    public ProgressWatcher(SimpMessageSendingOperations simpMessageSendingOperations) {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @Getter
    private State state = initializeState();

    private State initializeState() {
        return new State("not started", States.IDLE, -1, -1);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.state = (State) arg;
        logger.info("{}", state);
    }

    public void reset() {
        this.state = initializeState();
    }

    public void sendState() throws Exception {
        logger.info("working now, {}", new Date().getTime());
        simpMessageSendingOperations.convertAndSend("/topic/greetings", getState());
    }
}
