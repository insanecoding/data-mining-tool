package com.me.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ConfusionTable {
    List<String> categories;
    List<List<Double>> confMat;
}