package com.me.controller;

import com.me.domain.DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class MyRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("retrieve")
    public DTO returnDTO() {
        logger.info(" >>> [get] client connected");
        return new DTO("John", "Doe");
    }

    @PostMapping("add")
    public
    @ResponseBody
    DTO saveMe(@RequestBody DTO dto) {
        logger.info(" >>> [post] client connected");
        logger.info("data received: {}", dto);
        return new DTO("nice", "job");
    }
}
