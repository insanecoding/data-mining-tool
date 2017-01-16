package com.me.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DownloaderParameters {

    private int downloadsPerCategory;

    private int threadsNumber;

    private int readTimeOut;

    private int connectTimeOut;
}
