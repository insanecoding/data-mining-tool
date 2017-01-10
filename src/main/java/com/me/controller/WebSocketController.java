package com.me.controller;

import com.me.common.AsyncExecutor;
import com.me.common.ExecutableInitializer;
import com.me.common.MyExecutable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class WebSocketController {

    private final AsyncExecutor executor;
    private final ExecutableInitializer initializer;
    private boolean cancelFlag = false;

    @Autowired
    public WebSocketController(AsyncExecutor executor, ExecutableInitializer initializer) {
        this.executor = executor;
        this.initializer = initializer;
    }

    // when client sends cancel command to this address
    @MessageMapping("/cancel")
    public void cancel() {
        log.info(" >>> [websocket] execution was cancelled");
        cancelFlag = true;
        executor.stop();
    }

    // when client sends something to socket/progress
    @MessageMapping("/progress")
    // send the answer to the broker
    // it will broadcast the message to the subscribers
    @SendTo("/topic/broker")
    public Map<String, String> invoker(Map<String, Object> dto) {
        log.info(" >>> [websocket] client connected");
        List<MyExecutable> executables = initializer.createExecutables(dto);

        if (executables.size() != 0)
            executor.invokeAll(executables);

        return defineOutputMessage();
    }

    private Map<String, String> defineOutputMessage() {
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
