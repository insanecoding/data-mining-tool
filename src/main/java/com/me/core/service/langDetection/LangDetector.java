package com.me.core.service.langDetection;

public interface LangDetector {
    String detectLang(String text);

    /**
     * some language detectors may work with short text differently than with long one
     */
    default void setShortText(boolean isShortText) {}
}
