package com.me.core.service.experiment;

public enum Types {
    BINOMIAL("binomial"), REAL("real");

    private String type;

    Types(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
