package com.me.common;

public interface Executable {
    /**
     * do some stuff in order to correctly interrupt service
     */
    default void beforeCancel() {}

    void execute(Object... args) throws Exception;
}
