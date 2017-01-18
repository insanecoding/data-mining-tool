package com.me.core.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "dependent_experiment")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DependentExperiment implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entry_id")
    private Long paramId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="owner_id")
    @NonNull
    private Experiment owner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="dependent_id")
    @NonNull
    private Experiment dependent;
}
