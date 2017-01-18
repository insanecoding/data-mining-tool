package com.me.core.domain.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "experiment_param")

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "paramId")
public class ExperimentParam {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "param_id")
    private Long paramId;

    @Column(name = "IDF_Threshold")
    private double IDF_Threshold;

    @Column(name = "IDF_Type")
    private String IDF_Type;

    @Column(name = "TF_Type")
    private String TF_Type;

    @Column(name = "features_by_category")
    private int featuresByCategory;

    @Column(name = "n_gram_size")
    private int nGramSize;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "norm_ratio")
    private double normalizeRatio;

    @Column(name = "round_to_places")
    private int roundToDecimalPlaces;

}
