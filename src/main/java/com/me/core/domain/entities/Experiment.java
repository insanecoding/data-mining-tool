package com.me.core.domain.entities;

import com.me.core.service.experiment.Modes;
import com.me.core.service.experiment.Types;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "experiment")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "experimentNumber")
public class Experiment implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "exp_number")
    private Long experimentNumber;

    @Column(name = "exp_name", unique = true)
    @NonNull
    private String expName;

    @Column(name = "description")
    @NonNull
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="dataset_id")
    @NonNull
    private DataSet dataSet;

    @Enumerated(EnumType.STRING)
    @Column(name = "exp_mode")
    @NonNull
    private Modes mode;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @NonNull
    private Types type;
}
