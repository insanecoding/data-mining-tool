package com.me.core.service.rapidminer;


import com.me.core.domain.entities.Experiment;

import java.io.IOException;
import java.util.List;

public class SchemeGeneratorService {
    private String templatesPath;
    private String workingDir;
    private List<String> targetCategories;


    public void generateSchemes(Experiment experiment, List<String> categories) throws IOException {
        SchemeGenerator sg = new SchemeGenerator();
        sg.setTemplatesPath(templatesPath);
        sg.setWorkingDir(workingDir);

        for (String category : categories) {
            sg.generateBaseLearnersScheme(experiment,
                    categories.indexOf(category) + 1, category);
        }


        sg.generateStackingScheme(experiment, categories.size());
        sg.generateApplyModelScheme(experiment);
    }
}
