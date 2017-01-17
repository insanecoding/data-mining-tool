package com.me.core.service.experiment.text.aml;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.AbstractText;
import com.me.core.domain.entities.ChosenCategory;
import com.me.core.domain.entities.Experiment;
import com.me.core.domain.entities.ExperimentParam;
import com.me.core.service.dao.MyDao;
import com.me.core.service.experiment.text.TextDataProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
public class PrepareAMLDatService extends StoppableObservable implements MyExecutable{

    @Getter @Setter
    private List<String> expNames;

    @Getter @Setter
    private AMLDATUtility utility;
    @Getter @Setter
    private MyDao dao;
    @Getter @Setter
    private TextDataProvider provider;

    @Autowired
    public PrepareAMLDatService(AMLDATUtility utility, ProgressWatcher watcher,
                                MyDao dao, TextDataProvider provider) {
        super.addSubscriber(watcher);
        this.utility = utility;
        this.dao = dao;
        this.provider = provider;
    }

    @Override
    public void execute() throws Exception {
        List<Experiment> experiments = dao.findExperimentsByNames(expNames);
        experiments.sort(Comparator.comparing(Experiment::getExpName));

        for (Experiment experiment : experiments) {
            ExperimentParam currentParam = experiment.getExperimentParam();
            int count = currentParam.getFeaturesByCategory();
            super.updateMessage(experiment.getExpName() + ": preparing AMLs");
            utility.saveAMLs(experiment, new int[] {count});

            createDATForCategories(experiment, currentParam);
            utility.clear();
        }
    }

    private void createDATForCategories(Experiment experiment,
                                        ExperimentParam currentParam) throws Exception {
        List<ChosenCategory> chosenCategories =
                dao.findCategoriesByDataSet(experiment.getDataSet());

        for (ChosenCategory chosenCategory : chosenCategories) {
            // getting texts in this category
            List<? extends AbstractText> textsInCategory =
                    provider.provideTextData(experiment, currentParam, chosenCategory);
            super.updateMessage(experiment.getExpName() + ": creating dat for category: " +
                    chosenCategory.getCategory().getCategoryName());
            utility.saveDATs(experiment, textsInCategory, chosenCategory.getCategory());
        }
    }

    @Override
    public String getName() {
        return "AML/DAT preparation service";
    }

}
