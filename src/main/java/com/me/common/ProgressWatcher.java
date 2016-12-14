package com.me.common;

import com.me.core.domain.dto.State;
import com.me.core.domain.dto.States;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

@Component("progressWatcher")
public class ProgressWatcher implements Observer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Getter
    private State state = initializeState();
    private final WebSocketMessageSender sender;

    @Autowired
    public ProgressWatcher(WebSocketMessageSender sender) {
        this.sender = sender;
    }

    private State initializeState() {
        return new State("not started", States.IDLE,0);
    }

    @Override
    public void update(Observable o, Object arg) {
        // update state
        this.state = (State) arg;
        logger.info("{}", state);
        sender.sendMessage(state);
    }

    public void reset() {
        this.state = initializeState();
    }

}
