package com.me.common;

public interface MyExecutable {
    // invoke service
    void execute() throws Exception;

    // clean internal data before exit or cancel
    default void cleanUp() {}

    // get user-friendly service name (for output in progressbar)
    String getName();

    default void beforeCancel() {}

    default void afterFinish() {}
}
