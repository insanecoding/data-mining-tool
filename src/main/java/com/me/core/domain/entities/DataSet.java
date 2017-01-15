package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "datasets")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "dataSetId")
public class DataSet implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "dataset_id")
    private Long dataSetId;

    @Column(name = "name", unique = true)
    @NonNull
    private String name;

    @Column(name = "description")
    @NonNull
    private String description;

    @Column(name = "partition_learn")
    @NonNull
    private double partitionLearn;
}
