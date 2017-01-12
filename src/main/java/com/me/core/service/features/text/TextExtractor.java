package com.me.core.service.features.text;

import com.me.core.domain.entities.HTML;
import com.me.core.domain.entities.Tag;
import com.me.core.domain.entities.TextFromTag;
import com.me.core.domain.entities.TextMain;

import java.util.List;
import java.util.Optional;

public interface TextExtractor {
    Optional<TextMain> extractTextMain(HTML html);
    List<TextMain> extractTextMain(List<HTML> html) throws InterruptedException;

    Optional<TextFromTag> extractTextFromTag(HTML htmlContents, Tag tag);
    List<TextFromTag> extractTextFromTag(List<HTML> htmls, Tag tag) throws InterruptedException;
}
