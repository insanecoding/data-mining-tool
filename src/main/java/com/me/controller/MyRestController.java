package com.me.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class MyRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping()
    public String foo() {
        return logger.getClass().getName() + " " + logger.getName();
    }

    @PostMapping("add")
    public
    @ResponseBody
    String saveMe(@RequestBody List<String> strings) {
        logger.info("client connected");
        return "{ \"result\": \"ok\" }";
    }
}
