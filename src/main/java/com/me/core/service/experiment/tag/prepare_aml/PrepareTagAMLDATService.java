package com.me.core.service.experiment.tag.prepare_aml;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PrepareTagAMLDATService extends StoppableObservable implements MyExecutable {
    @Getter @Setter
    private List<String> expNames;

    private final MyDao dao;
    private final NormalizedTagStat tagStat;

    @Autowired
    public PrepareTagAMLDATService(MyDao dao, ProgressWatcher watcher,
                                   NormalizedTagStat tagStat) {
        super.addSubscriber(watcher);
        this.dao = dao;
        this.tagStat = tagStat;
    }

    private void prepareAML(Experiment experiment, int featuresByCategory) throws InterruptedException {
        List<DictionaryWords> words = dao.findDictionaryWords(experiment);

        List<AmlFile> amlFiles = words.stream().limit(featuresByCategory).map(word -> {
            String wordStr = word.getWord().split(" - ")[1];
            String feature = experiment.getExpName() + '_' + wordStr;
            return new AmlFile(feature, experiment);
        }).collect(Collectors.toList());
        dao.batchSave(amlFiles);
    }

    private void prepareDAT(Experiment experiment, int featuresByCategory) throws InterruptedException {
        List<DictionaryWords> words = dao.findDictionaryWords(experiment)
                .stream().limit(featuresByCategory).collect(Collectors.toList());
        DataSet dataSet = experiment.getDataSet();
        List<ChosenCategory> categories = dao.findCategoriesByDataSet(dataSet);

        processCategories(experiment, words, categories);
    }

    private void processCategories(Experiment experiment,
                                   List<DictionaryWords> words,
                                   List<ChosenCategory> categories) throws InterruptedException {
        for (ChosenCategory chosenCategory : categories) {
            Category category = chosenCategory.getCategory();
            super.updateMessage(experiment.getExpName() + ": creating dat for category:"
                    + chosenCategory.getCategory());
            String categoriesBasis = createCategoriesBasis(categories, category);

            List<DatFile> datInCategory = tagStat.calculateMetric(category, words,
                    experiment, categoriesBasis);
            dao.batchSave(datInCategory);
        }
    }

    private String createCategoriesBasis(List<ChosenCategory> categories,
                                         Category category) {
        String categoriesBasis = "";
        for (ChosenCategory cat : categories) {
            categoriesBasis += (cat.getCategory().getCategoryName()
                    .equals(category.getCategoryName()) ? "\"1\"" : "\"0\"") + " ";
        }
        categoriesBasis += category.getCategoryName();
        return categoriesBasis;
    }

    @Override
    public void execute() throws Exception {
        List<Experiment> experiments = dao.findExperimentsByNames(expNames);
        for (Experiment experiment : experiments) {
            ExperimentParam param = experiment.getExperimentParam();
            tagStat.setRoundToDecimalPlaces(param.getRoundToDecimalPlaces());
            tagStat.setNormalizeRatio(param.getNormalizeRatio());
            int featuresByCategory = param.getFeaturesByCategory();
            super.updateMessage(experiment.getExpName() + ": processing AML for tags");
            prepareAML(experiment, featuresByCategory);
            prepareDAT(experiment, featuresByCategory);
        }
    }

    @Override
    public String getName() {
        return "Prepare AML/DAT for tags";
    }
}
