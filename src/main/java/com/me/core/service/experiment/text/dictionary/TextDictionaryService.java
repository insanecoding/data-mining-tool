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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

            List<ChosenWebsite> chosenWebsites = dao.findChosenWebsites(
                    experiment.getDataSet(), chosenCategory.getCategory()
            );
            List<Long> chosenIDs = extractChosenWebsiteIDs(chosenWebsites);
            // getting texts in this category
            List<? extends AbstractText> textsInCategory = dao.findTextsForIDs(
                    chosenIDs, chosenCategory.getCategory(), experiment.getMode(), param
            );

            if (experiment.getMode().equals(Modes.TEXT_FROM_TAGS)) {
                textsInCategory = textFromTagsProcessing(param, chosenWebsites, chosenIDs, textsInCategory);
            } else if (experiment.getMode().equals(Modes.NGRAMS)) {
                textsInCategory = textFromNGramsProcessing(param, chosenWebsites, chosenIDs, textsInCategory);
            }
            chosenIDs.clear();

            super.updateMessage(experiment.getExpName() + ": creating local tf for category: " +
                    chosenCategory.getCategory().getCategoryName());
            utility.createLocalTFs(textsInCategory, chosenCategory.getCategory(),
                    isTFCorrect, experiment, stopWordsPath);
        }
    }

    private List<? extends AbstractText> textFromNGramsProcessing(DictionaryParam param,
                                                                  List<ChosenWebsite> chosenWebsites,
                                                                  List<Long> chosenIDs,
                                                                  List<? extends AbstractText> textsInCategory) {
        int nGramSize = param.getNGramSize();
        Map<Long, Website> IDsAndWebsites = createChosenIDsMap(chosenWebsites);
        List<NGrams> unknownTexts = findAndPopulateNgramUnknowns(chosenIDs, nGramSize,
                IDsAndWebsites, textsInCategory);
        textsInCategory = addUnknownsToNGrams(textsInCategory, IDsAndWebsites, unknownTexts);
        return textsInCategory;
    }

    @SuppressWarnings("unchecked")
    private List<? extends AbstractText> addUnknownsToNGrams(List<? extends AbstractText> textsInCategory,
                                                             Map<Long, Website> IDsAndWebsites,
                                                             List<NGrams> unknownTexts) {
        List<? extends AbstractText> temp = new ArrayList<>(textsInCategory);
        List<NGrams> tft = (List<NGrams>) temp;
        tft.addAll(unknownTexts);
        textsInCategory = tft;
        IDsAndWebsites.clear();
        return textsInCategory;
    }

    private List<NGrams> findAndPopulateNgramUnknowns(List<Long> chosenIDs, int nGramSize,
                                                      Map<Long, Website> IDsAndWebsites,
                                                      List<? extends AbstractText> textsInCategory) {
        List<Long> idsWithFeatures = extractAbstractTextIDs(textsInCategory);
        // now this ones are in ChosenWebsites and have text main features,
        // but not text from tags
        List<Long> unknowns = (List<Long>)
                CollectionUtils.subtract(chosenIDs, idsWithFeatures);

        // create dummy data for unknowns
        return unknowns.stream().map(unknown -> {
            Website ws = IDsAndWebsites.get(unknown);
            return new NGrams(ws, nGramSize, "");
        }).collect(Collectors.toList());
    }

    private List<? extends AbstractText> textFromTagsProcessing(DictionaryParam param,
                                                                List<ChosenWebsite> chosenWebsites,
                                                                List<Long> chosenIDs,
                                                                List<? extends AbstractText> textsInCategory) {
        String tagName = param.getTagName();
        Map<Long, Website> IDsAndWebsites = createChosenIDsMap(chosenWebsites);

        List<TextFromTag> unknownTexts = findAndPopulateTextFromTagUnknowns(chosenIDs, tagName,
                IDsAndWebsites, textsInCategory);

        textsInCategory = addUnknownsToTextsFromTags(textsInCategory, IDsAndWebsites, unknownTexts);
        return textsInCategory;
    }

    /**
     * returns map: WebsiteID - Website
     */
    private Map<Long, Website> createChosenIDsMap(List<ChosenWebsite> chosenWebsites) {
        return chosenWebsites.stream()
                .map(ChosenWebsite::getWebsite)
                .collect(Collectors.toMap(Website::getWebsiteId, Function.identity()));
    }

    private List<TextFromTag> findAndPopulateTextFromTagUnknowns(List<Long> chosenIDs,
                                                                 String tagName,
                                                                 Map<Long, Website> IDsAndWebsites,
                                                                 List<? extends AbstractText> textsInCategory) {
        List<Long> idsWithFeatures = extractAbstractTextIDs(textsInCategory);
        // now this ones are in ChosenWebsites and have text main features,
        // but not text from tags
        List<Long> unknowns = (List<Long>)
                CollectionUtils.subtract(chosenIDs, idsWithFeatures);

        // create object for current tag (wil be required for dummy unknowns)
        Tag tag = new Tag(tagName);

        // create dummy data for unknowns
        return unknowns.stream()
                .map(unknown -> {
                    Website ws = IDsAndWebsites.get(unknown);
                    return new TextFromTag(ws, tag, "", 0);
                })
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private List<? extends AbstractText> addUnknownsToTextsFromTags(List<? extends AbstractText> textsInCategory,
                                                                    Map<Long, Website> IDsAndWebsites,
                                                                    List<TextFromTag> unknownTexts) {
        List<? extends AbstractText> temp = new ArrayList<>(textsInCategory);
        List<TextFromTag> tft = (List<TextFromTag>) temp;
        tft.addAll(unknownTexts);
        textsInCategory = tft;
        IDsAndWebsites.clear();
        return textsInCategory;
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

    private List<Long> extractChosenWebsiteIDs(List<ChosenWebsite> websites) {
        return websites.stream()
                .map(elem -> elem.getWebsite().getWebsiteId())
                .collect(Collectors.toList());
    }

    private List<String> extractExperimentNames() {
        return dictionaryParams.stream()
                .map(param -> param.getExperiment().getExpName())
                .collect(Collectors.toList());
    }
}
