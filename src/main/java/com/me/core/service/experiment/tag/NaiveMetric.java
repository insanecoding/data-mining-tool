package com.me.core.service.experiment.tag;

import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NaiveMetric implements MetricCalculator {

    private final MyDao dao;

    @Autowired
    public NaiveMetric(MyDao dao) {
        this.dao = dao;
    }

    @Override
    public List<DatFile> calculateMetric(List<ChosenWebsite> chosenWebsites,
                                         List<DictionaryWords> words, Experiment experiment,
                                         String categoriesBasis) {
        List<DatFile> datInCategory = new LinkedList<>();

        int websitesProcessed = 0;
        for (ChosenWebsite website : chosenWebsites) {
            List<TagsInPage> tagsInPage = dao.findTagsInPage(website.getWebsite());
            List<String> tagsInPageStr = tagsInPage.stream()
                    .map(tagInPage -> tagInPage.getTag().getTagName())
                    .collect(Collectors.toList());
            String features = "";
            for (DictionaryWords dictionaryWord : words) {
                String[] tokens = dictionaryWord.getWord().split("_");
                String tagInDictionary = tokens[tokens.length - 1];
                features += ((tagsInPageStr.contains(tagInDictionary)) ? "\"1\"": "\"0\"") + " ";
            }

            int length = -1;
            boolean isUnknown = !features.contains("\"1\"");

            DatFile datFile = new DatFile(categoriesBasis, features,length,
                            website.getWebsite(), experiment, isUnknown);
            datInCategory.add(datFile);
            websitesProcessed++;
            if ((websitesProcessed % 10) == 0)
                log.info("processed: {}", websitesProcessed);

        }
        return datInCategory;
    }
}
