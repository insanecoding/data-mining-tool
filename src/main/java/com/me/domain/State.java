package com.me.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class State {
    @NonNull
    private String info;

    @NonNull
    private States state;

    @NonNull
    private int currentIteration;

    @NonNull
    private int maxIterations;
}
