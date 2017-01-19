package com.me.core.domain.dto;

public enum Modes {
    TEXT_MAIN("text_main"), TEXT_FROM_TAGS("text_from_tags"),
    NGRAMS("ngrams"), TAG_STAT("tag_stat"), JOIN("join");

    private String mode;

    Modes(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
