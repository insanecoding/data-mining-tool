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
    @JoinColumn(name="joined_id")
    @NonNull
    private JoinedExperiment joinedExperiment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="regular_id")
    @NonNull
    private RegularExperiment regularExperiment;
}
