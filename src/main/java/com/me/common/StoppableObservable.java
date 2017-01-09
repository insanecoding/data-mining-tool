package com.me.common;

import com.me.core.domain.dto.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

public abstract class StoppableObservable extends Observable{

    private State state = new State();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void updateInfo(String message, double current, double max) {
        if (current != -1 && max != -1) {
            double progress = calculateProgress(current, max);
            state.setProgress(progress);
        } else {
            state.setProgress(-1);
        }

        state.setInfo(message);

        setChanged();
        notifyObservers(state);
    }

    // new message and cancel check
    protected void updateMessageCheck(String message) throws InterruptedException {
        updateInfo(message, -1, -1);
        checkCancel();
    }

    // new message only
    protected void updateMessage(String message) {
        updateInfo(message, -1, -1);
    }

    // new message and progress
    void updateMessage(String message, double current, double max) {
        updateInfo(message, current, max);
    }

    // new progress only
    void updateMessage(double current, double max) {
        updateInfo("", current, max);
    }

    protected void updateMeta(String message) {
        updateInfo(message, 0, 1);
    }

    protected void updateWorking(String message, double current, double max) {
        updateInfo(message, current, max);
    }

    protected void updateWorking(double current, double max) {
        updateInfo("", current, max);
    }

    protected void updateWorkingCheck(String message, double current,
                                      double max) throws InterruptedException {
        updateInfo(message, current, max);
        checkCancel();
    }

    protected void updateAndCheck(String message, double current, double max)
            throws InterruptedException {
        updateInfo(message, current, max);
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
