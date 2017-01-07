package com.me.controller;

import com.me.common.AsyncExecutor;
import com.me.common.MyExecutable;
import com.me.core.service.importbl.BlacklistImporterService;
import com.me.core.service.uncompress.UncompressService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@Slf4j
public class MyRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AsyncExecutor executor;
    private MyExecutable executable = null;
    private boolean cancelFlag = false;
    @Lazy
    private final BlacklistImporterService importerService;
    @Lazy
    private final UncompressService uncompressService;


    @Autowired
    public MyRestController(AsyncExecutor executor,
                            BlacklistImporterService importerService, UncompressService uncompressService) {
        this.executor = executor;
        this.importerService = importerService;
        this.uncompressService = uncompressService;
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
    Map<String, Object> invokeService(@RequestBody Map<String, Map<String, Object>> dto) {
        log.info(" >>> [post] client connected");
        decisionMaker(dto);

        if (executable != null)
            executor.invoke(executable, false);

        return defineOutputMessage();
    }

    private Map<String, Object> defineOutputMessage() {
        Map<String, Object> result = new LinkedHashMap<>();

        if (!cancelFlag) {
            result.put("status", "finished");
        } else {
            cancelFlag = false;
            result.put("status", "cancelled");
        }
        result.put("percentsProgress", 0);
        return result;
    }

    @SuppressWarnings(value = "unchecked")
    private void decisionMaker(Map<String, Map<String, Object>> clientResponse) {
        Map<String, Object> settings =  clientResponse.get("import");
        final String cwd = (String) settings.get("cwd");

        if ((boolean)settings.get("isOn")) {
            List<LinkedHashMap<String, String>> blacklists =
                    (List<LinkedHashMap<String, String>>) settings.get("blacklists");
            List<String> compressed = blacklists.stream()
                    .map( k -> k.get("folderName"))
                    // concatenate to make absolute path from relative
                    .map( relativePath -> cwd + "\\" + relativePath)
                    .collect(Collectors.toList());
            executable = uncompressService;
            Map<String, Object> initSettings = new LinkedHashMap<>();
            initSettings.put("compressed", compressed);
            executable.initialize(initSettings);
        }
    }
}
