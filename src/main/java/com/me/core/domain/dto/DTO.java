package com.me.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DTO {
    private final String userName;

    private final String password;

    private final String dbName;

    private final int port;
}
