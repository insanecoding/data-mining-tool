package com.me.common;

public interface Executable {
    /**
     * do some stuff in order to correctly interrupt service
     */
    default void beforeCancel() {}

    /**
     * do some clean-up after stopping
     */
    void afterCancel();

    /**
     *
     * @param args possible service parameters
     * @throws Exception
     */
    void execute(Object... args) throws Exception;
}

// todo: create cancel with support for multiple rerun
