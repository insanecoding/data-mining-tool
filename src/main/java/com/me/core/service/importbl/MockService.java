//package com.me.core.service.importbl;
//
//import com.me.common.MyExecutable;
//import com.me.common.ProgressWatcher;
//import com.me.common.StoppableObservable;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MockService extends StoppableObservable implements MyExecutable {
//
//    @Autowired
//    public MockService(ProgressWatcher watcher) {
//        super.addSubscriber(watcher);
//    }
//
//    @Override
//    public void execute(Object... args) throws Exception {
//        super.updateMeta("just started");
//        double maxIteration = 50;
//        for (int i = 0; i < maxIteration; i++) {
//            for (int j = 1; j < 100_000_000; j++) {
//                super.checkCancel();
//            }
//            double iter = i;
//            updateWorkingCheck("I'm on iteration #" + ++iter, iter, maxIteration);
//        }
//    }
//}
//
