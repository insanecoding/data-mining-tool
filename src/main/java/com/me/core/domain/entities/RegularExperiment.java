package com.me.core.domain.entities;

import com.me.core.domain.dto.Modes;
import com.me.core.domain.dto.Types;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("R")
@NoArgsConstructor
public class RegularExperiment extends AbstractExperiment implements Serializable {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="dataset_id")
    private DataSet dataSet;

    @Enumerated(EnumType.STRING)
    @Column(name = "exp_mode")
    private Modes mode;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Types type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="param_id")
    private ExperimentParam experimentParam;
}
