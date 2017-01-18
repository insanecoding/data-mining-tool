package com.me.core.domain.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@DiscriminatorValue("J")
@NoArgsConstructor
public class JoinedExperiment extends AbstractExperiment implements Serializable{
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name="dependencies")
//    private List<RegularExperiment> dependencies;
}
