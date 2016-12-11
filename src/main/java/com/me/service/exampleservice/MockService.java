package com.me.service.exampleservice;

import com.me.domain.States;
import com.me.service.async.MyExecutable;
import com.me.service.async.ProgressWatcher;
import com.me.service.async.StoppableObservable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockService extends StoppableObservable implements MyExecutable {

    @Autowired
    public MockService(ProgressWatcher watcher) {
        super.addSubscriber(watcher);
    }

    @Override
    public void execute(Object... args) throws Exception {
        super.updateInfo("just started", States.META, -1, -1);
        for (int i = 0; i < 5; i++) {
            Thread.sleep(2000);
//            for (int j = 1; j < 100_000_000; j++) {
//                super.checkCancel();
//            }
            int iter = i;
            super.updateAndCheck("I'm on iteration #" + ++iter, States.WORKING, iter, 5);
        }
//        super.updateInfo("just finished", States.WORKING, 5, 5);
    }
}
