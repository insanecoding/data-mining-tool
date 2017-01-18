package com.me.core.service.experiment.tag.prepare_aml;

import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.DatFile;
import com.me.core.domain.entities.DictionaryWords;
import com.me.core.domain.entities.Experiment;

import java.util.List;
import java.util.Map;

public interface MetricCalculator {
    List<DatFile> calculateMetric(Category category,
                                  List<DictionaryWords> words,
                                  Experiment experiment,
                                  String categoriesBasis);

    default void init(Map<String, Object> params) {}
}
