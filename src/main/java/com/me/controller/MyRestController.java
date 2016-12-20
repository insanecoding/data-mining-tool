package com.me.controller;

import com.me.common.AsyncExecutor;
import com.me.common.MyExecutable;
import com.me.core.domain.dto.DTO;
import com.me.core.service.importbl.BlacklistImporterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("api")
public class MyRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AsyncExecutor executor;
    private MyExecutable executable;
    private boolean cancelFlag = false;
    private final BlacklistImporterService importerService;

    @Autowired
    public MyRestController(AsyncExecutor executor,
                            BlacklistImporterService importerService) {
        this.executor = executor;
        this.importerService = importerService;
    }

//    @GetMapping("result")
//    public DTO returnDTO() {
//        logger.info(" >>> [get] client connected");
//        return new DTO("Welcome,", "guest");
//    }

    private void decisionMaker(int num) {
        if (num == 2)
            executable = importerService;
        else
            executable = null;
    }

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

        decisionMaker(2);
//        executor.invoke(executable, false);
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
