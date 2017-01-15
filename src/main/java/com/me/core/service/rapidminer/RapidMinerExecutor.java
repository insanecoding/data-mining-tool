package com.me.core.service.rapidminer;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RapidMinerExecutor {

    private String pathToRM;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getPathToRM() {
        return pathToRM;
    }

    public void setPathToRM(String pathToRM) {
        this.pathToRM = pathToRM;
    }

    private void executeCommand(String command){
        CommandLine cmdLine = CommandLine.parse(command);
        DefaultExecutor executor = new DefaultExecutor();

        try {
            int exitValue = executor.execute(cmdLine);
            logger.info(" exited with {}", exitValue);
        } catch (IOException e) {
            logger.error("{} : {}", e.getClass(), e.getMessage());
        }
    }

    public void executeProcessInRM(String pathToProcess) {
        // add quotes to path (because it may contain whitespaces which cause errors in cmd)
        String line = "'" + pathToRM + "'" +
                " -f" +
                " '" + pathToProcess + "'";

        executeCommand(line);
    }
}
