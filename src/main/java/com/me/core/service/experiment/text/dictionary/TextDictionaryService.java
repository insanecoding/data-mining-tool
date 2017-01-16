package com.me.core.service.experiment.text.dictionary;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import com.me.core.service.experiment.Modes;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    public TextDictionaryService(MyDao dao,
                                 CreateDictionaryUtility utility,
                                 ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.dao = dao;
        this.utility = utility;
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
                          List<ChosenCategory> chosenCategories) throws IOException, InterruptedException {
        for (ChosenCategory chosenCategory : chosenCategories) {

            List<? extends AbstractText> textsInCategory = prepareTextData(experiment, param, chosenCategory);

            super.updateMessage(experiment.getExpName() + ": creating local tf for category: " +
                    chosenCategory.getCategory().getCategoryName());
            utility.createLocalTFs(textsInCategory, chosenCategory.getCategory(),
                    isTFCorrect, experiment, stopWordsPath);
        }
    }

    private List<? extends AbstractText> prepareTextData(Experiment experiment,
                                                         DictionaryParam param,
                                                         ChosenCategory chosenCategory) {
        List<ChosenWebsite> chosenWebsites = dao.findChosenWebsites(
                experiment.getDataSet(), chosenCategory.getCategory()
        );
        Map<Long, Website> IDsAndWebsites = createChosenIDsMap(chosenWebsites);
        List<Long> chosenIDs = new ArrayList<>(IDsAndWebsites.keySet());
        Modes mode = experiment.getMode();
        // getting texts in this category
        List<? extends AbstractText> textsInCategory = dao.findTextsForIDs(
                chosenIDs, chosenCategory.getCategory(), mode, param
        );

        if (mode.equals(Modes.TEXT_FROM_TAGS) || mode.equals(Modes.NGRAMS)) {
            textsInCategory = addUnknowns(param, IDsAndWebsites, chosenIDs,
                    textsInCategory, mode);
        }
        chosenIDs.clear();
        return textsInCategory;
    }

    private List<? extends AbstractText> addUnknowns(DictionaryParam param,
                                                     Map<Long, Website> IDsAndWebsites,
                                                     List<Long> chosenIDs,
                                                     List<? extends AbstractText> textsInCategory,
                                                     Modes mode) {
        List<Long> idsWithFeatures = extractAbstractTextIDs(textsInCategory);
        // now this ones are in ChosenWebsites and have text main features,
        // but not text from tags
        List<Long> unknowns = ListUtils.subtract(chosenIDs, idsWithFeatures);
        // create dummy data for unknowns
        Stream<Long> unknownIDsStream = unknowns.stream();
        List<? extends AbstractText> unknownTexts = createListByMode(param, IDsAndWebsites,
                mode, unknownIDsStream);

        IDsAndWebsites.clear();
        return ListUtils.union(textsInCategory, unknownTexts);
    }

    private List<? extends AbstractText> createListByMode(DictionaryParam param,
                                                          Map<Long, Website> IDsAndWebsites,
                                                          Modes mode, Stream<Long> unknownIDsStream) {
        List<? extends AbstractText> list;

        if (mode.equals(Modes.NGRAMS)) {
            list = unknownIDsStream.map(unknown -> {
                Website ws = IDsAndWebsites.get(unknown);
                return new NGrams(ws, param.getNGramSize(), "");
            }).collect(Collectors.toList());
        } else if (mode.equals(Modes.TEXT_FROM_TAGS)) {
            list = unknownIDsStream.map(unknown -> {
                Website ws = IDsAndWebsites.get(unknown);
                return new TextFromTag(ws, new Tag(param.getTagName()), "", 0);
            })
                    .collect(Collectors.toList());
        } else {
            list = null;
        }
        return list;
    }

    /**
     * returns map: WebsiteID - Website
     */
    private Map<Long, Website> createChosenIDsMap(List<ChosenWebsite> chosenWebsites) {
        return chosenWebsites.stream()
                .map(ChosenWebsite::getWebsite)
                .collect(Collectors.toMap(Website::getWebsiteId, Function.identity()));
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

    /**
     * returns list of ids for the websites that have desired features
     */
    private List<Long> extractAbstractTextIDs(List<? extends AbstractText> result) {
        return result.stream()
                .map(elem -> elem.getWebsite().getWebsiteId())
                .collect(Collectors.toList());
    }

    private List<String> extractExperimentNames() {
        return dictionaryParams.stream()
                .map(param -> param.getExperiment().getExpName())
                .collect(Collectors.toList());
    }
}
