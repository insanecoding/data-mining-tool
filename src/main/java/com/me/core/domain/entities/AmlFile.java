package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "aml_files")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "entry_id")
public class AmlFile implements Serializable{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entry_id")
    private Long entry_id;

    @Column(name = "feature_val", columnDefinition="TEXT")
    @NonNull
    private String feature;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="exp_number")
    @NonNull
    private Experiment experiment;
}
