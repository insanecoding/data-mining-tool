package com.me.core.service.experiment.tag;

import com.me.common.MyExecutable;
import com.me.common.ProgressWatcher;
import com.me.common.StoppableObservable;
import com.me.core.domain.entities.DictionaryWords;
import com.me.core.domain.entities.Experiment;
import com.me.core.service.dao.MyDao;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TagDictionaryCreator extends StoppableObservable implements MyExecutable {

    @Getter @Setter
    private List<String> expNames;

    private final MyDao dao;

    @Autowired
    public TagDictionaryCreator(MyDao dao, ProgressWatcher watcher) {
        super.addSubscriber(watcher);
        this.dao = dao;
    }

    @Override
    public void execute() throws Exception {
        List<Experiment> experiments = dao.findExperimentsByNames(expNames);
        experiments.sort(Comparator.comparing(Experiment::getExpName));

        for (Experiment experiment : experiments) {
            super.updateMessage(experiment.getExpName() + ": creating tag dictionary");
            List<String> tags = dao.findTopTags(experiment);
            List<DictionaryWords> words = createDictionaryWords(experiment, tags);
            dao.batchSave(words);
        }
    }

    private List<DictionaryWords> createDictionaryWords(Experiment experiment, List<String> tags) {
        return tags.stream().map(tag -> {
                    int num = tags.indexOf(tag);
                    String word = "01 - tag_" + num + "_" + tag;
                    return new DictionaryWords(experiment, word);
                }).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return "Tag dictionary creator";
    }
}
