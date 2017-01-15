package com.me.common;

import java.util.Map;

public interface MyExecutable {
    // invoke service
    void execute() throws Exception;

    // set up service parameters
    void initialize(Map<String, Object> param);

    // clean internal data before exit or cancel
    default void cleanUp() {}

    // get user-friendly service name (for output in progressbar)
    String getName();

    default void beforeCancel() {}

    default void afterFinish() {}
}
