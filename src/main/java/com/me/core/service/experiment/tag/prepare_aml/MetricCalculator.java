package com.me.core.service.experiment.tag.prepare_aml;

import com.me.core.domain.entities.ChosenWebsite;
import com.me.core.domain.entities.DatFile;
import com.me.core.domain.entities.Experiment;

import java.util.List;

public interface MetricCalculator {

    List<DatFile> calculateMetric(List<ChosenWebsite> chosen,
                                  Experiment experiment,
                                  String categoriesBasis);

    default void init(Experiment experiment) {
    }

    default void clear() {
    }
}