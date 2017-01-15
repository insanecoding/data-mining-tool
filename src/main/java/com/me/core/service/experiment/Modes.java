package com.me.core.service.experiment;

public enum Modes {
    TEXT_MAIN("text_main"), TEXT_FROM_TAGS("text_from_tags"),
    NGRAMS("ngrams"), TAG_STAT("tag_stat");

    private String mode;

    Modes(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
