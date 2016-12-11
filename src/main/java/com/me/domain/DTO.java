package com.me.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DTO {
    private final String firstName;

    private final String lastName;
}
