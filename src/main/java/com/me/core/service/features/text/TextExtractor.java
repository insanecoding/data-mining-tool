package com.me.core.service.features.text;

import com.me.core.domain.entities.HTML;
import com.me.core.domain.entities.Tag;
import com.me.core.domain.entities.TextFromTag;
import com.me.core.domain.entities.TextMain;

import java.util.List;

public interface TextExtractor {
    List<TextMain> extractTextMain(List<HTML> html) throws InterruptedException;

    List<TextFromTag> extractTextFromTag(List<HTML> htmls, Tag tag) throws InterruptedException;
}
