package com.me.core.service.experiment.text.dictionary;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.AbstractText;
import com.me.core.domain.entities.ChosenCategory;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.dao.MyDao;
import com.me.core.service.experiment.text.TextDataProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TextDictionaryService extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private List<DictionaryParam> dictionaryParams;
    @Getter @Setter
    private String stopWordsPath;

    @Getter @Setter
    private MyDao dao;
    @Getter @Setter
    private CreateDictionaryUtility utility;
    @Getter @Setter
    private TextDataProvider dataProvider;

    @Autowired
    public TextDictionaryService(MyDao dao,
                                 CreateDictionaryUtility utility,
                                 ProgressWatcher watcher, TextDataProvider dataProvider) {
        super.addSubscriber(watcher);
        this.dao = dao;
        this.utility = utility;
        this.dataProvider = dataProvider;
    }

    @Override
    public void execute() throws Exception {

        List<String> expNames = extractExperimentNames();

        List<Experiment> experiments = dao.findExperimentsByNames(expNames);
        for (Experiment experiment : experiments) {

            int current = experiments.indexOf(experiment);
            boolean isTFCorrect =
                    dictionaryParams.get(current).getTF_Type().matches("[Ss]");
            boolean isIDFCorrect =
                    dictionaryParams.get(current).getIDF_Type().matches("[Ss]");
            double threshold = dictionaryParams.get(current).getIDF_Treshold();

            List<ChosenCategory> chosenCategories =
                    dao.findCategoriesByDataSet(experiment.getDataSet());
            createTF(experiment, dictionaryParams.get(current), isTFCorrect, chosenCategories);

            createOtherMetrics(experiment, isIDFCorrect, threshold, chosenCategories);
            utility.saveDictionary(experiment);
            utility.clear();
        }
    }

    private void createTF(Experiment experiment, DictionaryParam param, boolean isTFCorrect,
                          List<ChosenCategory> chosenCategories) throws Exception {
        for (ChosenCategory chosenCategory : chosenCategories) {

            List<? extends AbstractText> textsInCategory = dataProvider.provideTextData(experiment, param, chosenCategory);

            super.updateMessage(experiment.getExpName() + ": creating local tf for category: " +
                    chosenCategory.getCategory().getCategoryName());
            utility.createLocalTFs(textsInCategory, chosenCategory.getCategory(),
                    isTFCorrect, experiment, stopWordsPath);
        }
    }


    private void createOtherMetrics(Experiment experiment, boolean isIDFCorrect, double threshold,
                                    List<ChosenCategory> chosenCategories) throws Exception {
        super.updateMessage(experiment.getExpName() + ": creating idf list");
        utility.createIDFList(isIDFCorrect, threshold, experiment, chosenCategories.size());

        for (ChosenCategory cat : chosenCategories) {
            super.updateMessage(experiment.getExpName() + ": creating tfidf list for "
                    + cat.getCategory().getCategoryName());
            utility.createTFIDFList(cat.getCategory(),
                    chosenCategories.indexOf(cat) + 1, experiment);
        }
    }

    @Override
    public String getName() {
        return "Create dictionary service";
    }

    private List<String> extractExperimentNames() {
        return dictionaryParams.stream()
                .map(param -> param.getExperiment().getExpName())
                .collect(Collectors.toList());
    }
}
