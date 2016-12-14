package com.me.service.async;

import com.me.domain.State;
import com.me.domain.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

public abstract class StoppableObservable extends Observable{

    private State state = new State();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void updateInfo(String message, States currentState, int currentIteration,
                              int maxIterations) {
        state.setCurrentIteration(currentIteration);
        state.setState(currentState);
        state.setMaxIterations(maxIterations);
        state.setInfo(message);

        setChanged();
        notifyObservers(state);
    }

    protected void updateMeta(String message) {
        updateInfo(message, States.META, -1, -1);
    }

    protected void checkCancel() throws InterruptedException {
        if (Thread.interrupted()) {
            logger.warn("cancel called at {}", this.getClass().getName());
            throw new InterruptedException();
        }
    }

    protected void addSubscriber(ProgressWatcher watcher) {
        this.addObserver(watcher);
    }

    /**
     * convenience method: creates the message to update and checks for cancel command
     */
    protected void updateAndCheck(String message, States currentState, int currentIteration,
                                  int maxIterations) throws InterruptedException {
        updateInfo(message, currentState, currentIteration, maxIterations);
        checkCancel();
    }
}
