package com.me.core.service.experiment.text.output;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.dto.AmlDatPath;
import com.me.core.domain.entities.AmlFile;
import com.me.core.domain.entities.ChosenCategory;
import com.me.core.domain.entities.DatFile;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AMLDATWriter extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private String outputFolder;
    @Getter @Setter
    private List<String> expNames;

    @Getter @Setter
    private MyDao dao;
    @Getter @Setter
    private AmlWriter amlWriter;
    @Getter @Setter
    private DatWriter datWriter;

    @Autowired
    public AMLDATWriter(MyDao dao, AmlWriter amlWriter,
                        ProgressWatcher watcher, DatWriter datWriter) {
        super.addSubscriber(watcher);
        this.dao = dao;
        this.amlWriter = amlWriter;
        this.datWriter = datWriter;
    }

    @Override
    public void execute() throws Exception {
        List<Experiment> experiments = dao.findExperimentsByNames(expNames);
        experiments.sort(Comparator.comparing(Experiment::getExpName));

        for (Experiment experiment : experiments) {

            List<String> categoryNames = getCategoryNames(experiment);
            List <AmlFile> amlFeatures = dao.findAMLByExperiment(experiment);

            createAmlDatForSubset(experiment, categoryNames, amlFeatures, true);
            createAmlDatForSubset(experiment, categoryNames, amlFeatures, false);
        }
    }

    private void createAmlDatForSubset(Experiment experiment, List<String> categoryNames,
                                       List<AmlFile> amlFeatures, boolean isLearn) throws IOException {
        AmlDatPath amlDatPath = createPaths(experiment, isLearn);
        amlWriter.createExperimentAML(experiment, categoryNames, amlFeatures, amlDatPath);

        List<Long> idsForSubset = dao.findIDsForSubset(experiment, isLearn);
        List<DatFile> dat = dao.findDatFilesForExperiment(experiment, idsForSubset);
        int categoriesNum = categoryNames.size();
        String datPath = amlDatPath.getDatPath();
        datWriter.createDATForExperiment(experiment, dat, isLearn, datPath, categoriesNum);
    }

    private List<String> getCategoryNames(Experiment experiment) {
        List<ChosenCategory> chosenCategories =
                dao.findCategoriesByDataSet(experiment.getDataSet());

        // sorted category names from category objects
        return chosenCategories.stream()
                .map(category -> category.getCategory().getCategoryName())
                .sorted()
                .collect(Collectors.toList());
    }

    private AmlDatPath createPaths(Experiment experiment, boolean isLearn) {
        String fileNamePart = outputFolder + "//" +
                experiment.getExpName().replaceAll("[^\\p{Alnum}]+", "");
        fileNamePart += "//" + experiment.getExpName()
                .replaceAll("[^\\p{Alnum}]+", "");

        String aml_path;
        String dat_path;

        if (isLearn) {
            // learn subset
             aml_path = fileNamePart + "_learn.aml";
             dat_path = fileNamePart + "_learn.dat";
        } else {
            // test subset
            aml_path = fileNamePart + "_test.aml";
            dat_path = fileNamePart + "_test.dat";
        }

        return new AmlDatPath(aml_path, dat_path);
    }

    @Override
    public String getName() {
        return "AML/DAT writer";
    }
}
