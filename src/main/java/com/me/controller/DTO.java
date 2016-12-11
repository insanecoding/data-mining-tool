package com.me.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
class DTO {
    private final String firstName;

    private final String lastName;
}
