package com.me.core.service.rapidminer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
class RapidMinerExecutor {

    private void executeCommand(String command) throws IOException {
        CommandLine cmdLine = CommandLine.parse(command);
        DefaultExecutor executor = new DefaultExecutor();

        int exitValue = executor.execute(cmdLine);
        log.info(" exited with {}", exitValue);
    }

    void executeProcessInRM(String pathToRM,
                            String pathToProcess) throws IOException {
        // add quotes to path (because it may contain whitespaces which cause errors in cmd)
        String line = "'" + pathToRM + "'" +
                " -f" +
                " '" + pathToProcess + "'";

        executeCommand(line);
    }
}
