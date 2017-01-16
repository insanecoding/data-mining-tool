package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "dat_files")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "entry_id")
public class DatFile implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entry_id")
    private Long entry_id;

    @Column(name = "categ_basis", columnDefinition="TEXT")
    @NonNull
    private String categoryBasis;

    @Column(name = "features", columnDefinition="TEXT")
    @NonNull
    private String features;

    @Column(name = "length")
    @NonNull
    private int length;

    @OneToOne
    @JoinColumn(name="website_id")
    @NonNull
    private Website website;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="exp_number")
    @NonNull
    private Experiment experiment;

    @Column(name = "isUnknown")
    @NonNull
    private boolean isUnknown;
}
