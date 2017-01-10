//package com.me.controller;
//
//import com.me.common.AsyncExecutor;
//import com.me.common.ExecutableInitializer;
//import com.me.common.MyExecutable;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("api")
//@Slf4j
//public class MyRestController {
//
//    private final AsyncExecutor executor;
//    private boolean cancelFlag = false;
//    private final ExecutableInitializer initializer;
//
//    @Autowired
//    public MyRestController(AsyncExecutor executor, ExecutableInitializer initializer) {
//        this.executor = executor;
//        this.initializer = initializer;
//    }
//
////    @GetMapping("cancel")
////    public void cancelService() {
////        log.info(" >>> [get] client connected");
////        cancelFlag = true;
////        executor.stop();
////    }
//
//    @PostMapping("invoke")
//    public
//    @ResponseBody
//    Map<String, Object> invokeService(@RequestBody Map<String, Map<String, Object>> dto) {
//        log.info(" >>> [post] client connected");
////        List<MyExecutable> executables = initializer.createExecutables(dto);
////
////        if (executables.size() != 0)
////            executor.invokeAll(executables);
////
//        return defineOutputMessage();
//    }
//
//    private Map<String, Object> defineOutputMessage() {
//        Map<String, Object> result = new LinkedHashMap<>();
//
//        if (!cancelFlag) {
//            result.put("status", "finished");
//        } else {
//            cancelFlag = false;
//            result.put("status", "cancelled");
//        }
//        // reset progress bar
//        result.put("percentsProgress", 0);
//        return result;
//    }
//}
