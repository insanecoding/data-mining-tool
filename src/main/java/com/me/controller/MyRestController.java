package com.me.controller;

import com.me.domain.DTO;
import com.me.service.async.AsyncExecutor;
import com.me.service.exampleservice.MockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class MyRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AsyncExecutor executor;
    private final MockService service;
    private boolean cancelFlag = false;

    @Autowired
    public MyRestController(AsyncExecutor executor, @Qualifier("mockService") MockService service) {
        this.executor = executor;
        this.service = service;
    }

//    @GetMapping("result")
//    public DTO returnDTO() {
//        logger.info(" >>> [get] client connected");
//        return new DTO("Welcome,", "guest");
//    }

    @GetMapping("cancel")
    public void cancelService() {
        logger.info(" >>> [get] client connected");
        cancelFlag = true;
        executor.stop();
    }

    @PostMapping("invoke")
    public
    @ResponseBody
    Map<String, String> invokeService(@RequestBody DTO dto) {
        logger.info(" >>> [post] client connected");
        logger.info("data received: {}", dto);

        executor.invoke(service, false);
        Map<String, String> result = new LinkedHashMap<>();

        if (!cancelFlag) {
            result.put("status", "finished");
        } else {
            cancelFlag = false;
            result.put("status", "cancelled");
        }
        return result;
    }
}
