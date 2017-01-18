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
    private final MetricCalculator tagStat;

    @Autowired
    public PrepareTagAMLDATService(MyDao dao, ProgressWatcher watcher,
                                   MetricCalculator tagStat) {
        super.addSubscriber(watcher);
        this.dao = dao;
        this.tagStat = tagStat;
    }

    private void prepareAML(Experiment experiment) throws InterruptedException {
        List<DictionaryWords> words = dao.findDictionaryWords(experiment);
        int featuresByCategory = experiment.getExperimentParam().getFeaturesByCategory();

        List<AmlFile> amlFiles = words.stream().limit(featuresByCategory).map(word -> {
            String wordStr = word.getWord().split(" - ")[1];
            String feature = experiment.getExpName() + '_' + wordStr;
            return new AmlFile(feature, experiment);
        }).collect(Collectors.toList());
        dao.batchSave(amlFiles);
    }

    private void prepareDAT(Experiment experiment) throws InterruptedException {
        tagStat.init(experiment);
        DataSet dataSet = experiment.getDataSet();
        List<ChosenCategory> categories = dao.findCategoriesByDataSet(dataSet);

        for (ChosenCategory chosenCategory : categories) {
            processCategoryContents(experiment, chosenCategory, categories);
        }
    }

    private void processCategoryContents(Experiment experiment, ChosenCategory chosenCategory,
                                         List<ChosenCategory> categories) throws InterruptedException {
        Category category = chosenCategory.getCategory();
        super.updateMessage(experiment.getExpName() + ": creating dat for category:"
                + chosenCategory.getCategory().getCategoryName());
        String categoriesBasis = createCategoriesBasis(categories, category);

        List<ChosenWebsite> chosenWebsites =
                dao.findChosenWebsites(experiment.getDataSet(), chosenCategory.getCategory());

        List<DatFile> datInCategory = tagStat.calculateMetric(chosenWebsites,
                experiment, categoriesBasis);
        dao.batchSave(datInCategory);
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
            super.updateMessage(experiment.getExpName() + ": processing AML for tags");
            prepareAML(experiment);
            prepareDAT(experiment);
            tagStat.clear();
        }
    }

    @Override
    public String getName() {
        return "Prepare AML/DAT for tags";
    }
}
