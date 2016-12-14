package com.me.controller;

import com.me.domain.DTO;
import com.me.service.async.AsyncExecutor;
import com.me.service.exampleservice.MockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class MyRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AsyncExecutor executor;
    private final MockService service;

    @Autowired
    public MyRestController(AsyncExecutor executor, @Qualifier("mockService") MockService service) {
        this.executor = executor;
        this.service = service;
    }

    @GetMapping("result")
    public DTO returnDTO() {
        logger.info(" >>> [get] client connected");
        return new DTO("Welcome,", "guest");
    }

    @PostMapping("invoke")
    public
    @ResponseBody
    DTO saveMe(@RequestBody DTO dto) {
        logger.info(" >>> [post] client connected");
        logger.info("data received: {}", dto);

        executor.invoke(service, false);

        return new DTO("Some", "result");
    }
}
