package com.me.common.initializer;

import com.me.common.MyExecutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExecutableCreator {

    private final UncompressImportInitializer first;

    @Autowired
    public ExecutableCreator(UncompressImportInitializer first) {
        this.first = first;
    }

    public List<MyExecutable> createExecutables(Map<String, Object> dto) {
        List<MyExecutable> executables = new ArrayList<>();
        first.initialize(dto, executables);
        return executables;
    }

    static List<String> createListFromMap(List<Map<String, Object>> categoriesTemp) {
        return categoriesTemp.stream()
                .map(elem -> (String)elem.get("name")).collect(Collectors.toList());
    }
}
