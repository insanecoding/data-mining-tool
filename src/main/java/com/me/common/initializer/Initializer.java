package com.me.common.initializer;

import com.me.common.MyExecutable;

import java.util.List;
import java.util.Map;

public interface Initializer {
    void initialize(Map<String, Object> dto, List<MyExecutable> executables);
}
