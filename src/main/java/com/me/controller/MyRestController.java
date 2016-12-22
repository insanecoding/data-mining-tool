package com.me.controller;

import com.me.common.AsyncExecutor;
import com.me.common.MyExecutable;
import com.me.core.service.importbl.BlacklistImporterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
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

    @GetMapping("cancel")
    public void cancelService() {
        logger.info(" >>> [get] client connected");
        cancelFlag = true;
        executor.stop();
    }

    @PostMapping("invoke")
    public
    @ResponseBody
    Map<String, String> invokeService(@RequestBody List<Map<String, String>> dto) {
        outputClientData(dto);
        decisionMaker(2);
        executor.invoke(executable, false);
        Map<String, String> result = new LinkedHashMap<>();

        if (!cancelFlag) {
            result.put("status", "finished");
        } else {
            cancelFlag = false;
            result.put("status", "cancelled");
        }
        return result;
    }

    private void outputClientData(@RequestBody List<Map<String, String>> dto) {
        logger.info(" >>> [post] client connected");
        dto.forEach( k -> {
            logger.info(" === starting output for k: {}", k);
            k.forEach( (key, val) -> logger.info("{} = {}", key, val));
            logger.info(" === finishing output for k: {}", k);
        });
    }

    private void decisionMaker(int num) {
        if (num == 2)
            executable = importerService;
        else
            executable = null;
    }
}
