package com.me.common;

import com.me.core.domain.dto.State;
import com.me.core.domain.dto.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

public abstract class StoppableObservable extends Observable{

    private State state = new State();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void updateInfo(String message, States currentState, double current, double max) {
        double progress = calculateProgress(current, max);
        state.setInfo(message);
        state.setProgress(progress);
        state.setState(currentState);
        setChanged();
        notifyObservers(state);
    }

    protected void updateMetaCheck(String message) throws InterruptedException {
        updateInfo(message, States.META, 0, 1);
        checkCancel();
    }

    protected void updateMeta(String message) {
        updateInfo(message, States.META, 0, 1);
    }

    protected void updateWorking(String message, double current, double max) {
        updateInfo(message, States.WORKING, current, max);
    }

    protected void updateWorkingCheck(String message, double current,
                                      double max) throws InterruptedException {
        updateInfo(message, States.WORKING, current, max);
        checkCancel();
    }

    protected void updateAndCheck(String message, States currentState, double current, double max)
            throws InterruptedException {
        updateInfo(message, currentState, current, max);
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

    private double calculateProgress(double current, double max) {
        return Math.round(current / max * 100);
    }
}
