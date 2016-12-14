package com.me.service.async;

import com.me.domain.State;
import com.me.domain.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

public abstract class StoppableObservable extends Observable{

    private State state = new State();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void updateInfo(String message, States currentState, double progress) {
        state.setInfo(message);
        state.setProgress(progress);
        state.setState(currentState);
        setChanged();
        notifyObservers(state);
    }

    protected void updateMeta(String message) {
        updateInfo(message, States.META, 0);
    }

    protected void updateWorkingCheck(String message, double progress) throws InterruptedException {
        updateInfo(message, States.WORKING, progress);
        checkCancel();
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
    protected void updateAndCheck(String message, States currentState, int progress)
            throws InterruptedException {
        updateInfo(message, currentState, progress);
        checkCancel();
    }

    protected double calculateProgress(double current, double max) {
        return Math.round(current / max * 100);
    }
}
