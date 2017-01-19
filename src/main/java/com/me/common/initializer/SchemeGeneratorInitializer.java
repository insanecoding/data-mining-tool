package com.me.common.initializer;

import com.me.common.MyExecutable;
import com.me.core.service.rapidminer.SchemeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SchemeGeneratorInitializer implements Initializer {


    private final SchemeGeneratorService schemeGenerator;

    @Autowired
    public SchemeGeneratorInitializer(SchemeGeneratorService schemeGenerator) {
        this.schemeGenerator = schemeGenerator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Map<String, Object> dto, List<MyExecutable> executables) {
        Map<String, Object> settings = (Map<String, Object>) dto.get("schemes");

        if ((boolean) settings.get("isOn")) {
            List<Map<String, Object>> experiments =
                    (List<Map<String, Object>>) settings.get("experiments");
            String templatesFolder = (String) settings.get("templatesFolder");
            Map<String, Object> formImport = (Map<String, Object>) dto.get("import");
            String cwd = (String) formImport.get("cwd");

            executables.add();
        }

    }
}
