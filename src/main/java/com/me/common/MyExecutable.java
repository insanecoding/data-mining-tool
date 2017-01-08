package com.me.common;

import java.util.Map;

public interface MyExecutable {
    void execute() throws Exception;

    void initialize(Map<String, Object> param);

    default void cleanUp() {}

    String getName();
}
