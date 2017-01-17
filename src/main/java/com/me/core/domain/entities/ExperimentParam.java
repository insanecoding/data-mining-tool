package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "experiment_param")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "paramId")
public class ExperimentParam {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "param_id")
    private Long paramId;

    @Column(name = "IDF_Threshold")
    @NonNull
    private double IDF_Threshold;

    @Column(name = "IDF_Type")
    @NonNull
    private String IDF_Type;

    @Column(name = "TF_Type")
    @NonNull
    private String TF_Type;

    @Column(name = "features_by_category")
    @NonNull
    private int featuresByCategory;

    @Column(name = "n_gram_size")
    private int nGramSize;

    @Column(name = "tag_name")
    private String tagName;
}
