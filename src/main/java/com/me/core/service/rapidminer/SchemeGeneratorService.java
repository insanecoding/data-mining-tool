package com.me.core.service.rapidminer;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.dto.Modes;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.ChosenCategory;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SchemeGeneratorService extends StoppableObservable implements MyExecutable {
    @Getter @Setter
    private String templatesPath;
    @Getter @Setter
    private String workingDir;
    @Getter @Setter
    private List<String> expNames;

    private final SchemeGenerator schemeGenerator;
    private final MyDao dao;

    @Autowired
    public SchemeGeneratorService(SchemeGenerator schemeGenerator,
                                  ProgressWatcher watcher, MyDao dao) {
        super.addSubscriber(watcher);
        this.schemeGenerator = schemeGenerator;
        this.dao = dao;
    }

    @Override
    public void execute() throws Exception {
        schemeGenerator.setTemplatesPath(templatesPath);
        workingDir = workingDir.replaceAll("\\\\", "/");
        schemeGenerator.setWorkingDir(workingDir);

        List<Experiment> experiments = dao.findExperimentsByNames(expNames);
        for (Experiment experiment : experiments) {
            super.updateMessage("generating schemes for: " + experiment.getExpName());
            List<ChosenCategory> categories =
                    dao.findCategoriesByDataSet(experiment.getDataSet());
            List<String> strCategories = createSortedStrCategories(categories);
            generateForExperiment(experiment, strCategories);
        }
    }

    private void generateForExperiment(Experiment experiment,
                                       List<String> strCategories) throws IOException {
        if (
                experiment.getMode().equals(Modes.TEXT_MAIN) ||
                experiment.getMode().equals(Modes.TEXT_FROM_TAGS) ||
                experiment.getMode().equals(Modes.NGRAMS)
                ) {
            for (String strCategory : strCategories) {
                int indexOfCategory = strCategories.indexOf(strCategory) + 1;
                String expName = experiment.getExpName().replaceAll("[^A-Z0-9a-z_]", "");
                schemeGenerator.generateBaseLearnersScheme(expName, indexOfCategory,  strCategory);
            }
            schemeGenerator.generateStackingScheme(experiment.getExpName(), strCategories.size());
            schemeGenerator.generateApplyModelScheme(experiment.getExpName());
        } else if (experiment.getMode().equals(Modes.TAG_STAT)) {
            schemeGenerator.generateTagStatScheme(experiment.getExpName());
        }
    }

    private List<String> createSortedStrCategories(List<ChosenCategory> categories) {
        return categories.stream()
                .map(ChosenCategory::getCategory)
                .sorted(Comparator.comparing(Category::getCategoryName))
                .map(Category::getCategoryName).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return "Scheme generator";
    }
}
