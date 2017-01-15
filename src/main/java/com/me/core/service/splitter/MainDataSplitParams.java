package com.me.core.service.splitter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainDataSplitParams {
    private String lang;
    private int minTextLength;
    private int maxTextLength;
    private int websitesPerCategory;
}
