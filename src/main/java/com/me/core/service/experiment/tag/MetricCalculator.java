package com.me.core.service.experiment.tag;

import com.me.core.domain.entities.ChosenWebsite;
import com.me.core.domain.entities.DatFile;
import com.me.core.domain.entities.DictionaryWords;
import com.me.core.domain.entities.Experiment;

import java.util.List;

public interface MetricCalculator {
    List<DatFile> calculateMetric(List<ChosenWebsite> chosenWebsites,
                                  List<DictionaryWords> words,
                                  Experiment experiment,
                                  String categoriesBasis);
    default void init(Object... params) {
    }
}
