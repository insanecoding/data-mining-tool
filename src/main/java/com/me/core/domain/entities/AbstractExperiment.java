package com.me.core.domain.entities;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Inheritance
@DiscriminatorColumn(name="exp_type")
@Table(name="experiment_2")

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "experimentNumber")
abstract class AbstractExperiment implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exp_number")
    private Long experimentNumber;

    @Column(name = "exp_name", unique = true)
    private String expName;

    @Column(name = "description")
    private String description;
}
