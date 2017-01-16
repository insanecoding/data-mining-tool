package com.me.core.service.experiment.text.dictionary;

import com.me.core.domain.entities.Experiment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryParam {

    private String dataSetName;

    private Experiment experiment;

    private double IDF_Treshold;

    private String IDF_Type;

    private String TF_Type;

    private int featuresByCategory;

    private int nGramSize;

    private String tagName;
}
