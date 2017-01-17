package com.me.core.service.experiment.text;

import com.me.core.domain.dto.Modes;
import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TextDataProvider {

    private final MyDao dao;

    @Autowired
    public TextDataProvider(MyDao dao) {
        this.dao = dao;
    }

    public List<? extends AbstractText> provideTextData(Experiment experiment,
                                                         ExperimentParam param,
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

    private List<? extends AbstractText> addUnknowns(ExperimentParam param,
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

    private List<? extends AbstractText> createListByMode(ExperimentParam param,
                                                          Map<Long, Website> IDsAndWebsites,
                                                          Modes mode, Stream<Long> unknownIDsStream) {
        List<? extends AbstractText> list = null;

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

    /**
     * returns list of ids for the websites that have desired features
     */
    private List<Long> extractAbstractTextIDs(List<? extends AbstractText> result) {
        return result.stream()
                .map(elem -> elem.getWebsite().getWebsiteId())
                .collect(Collectors.toList());
    }
}
