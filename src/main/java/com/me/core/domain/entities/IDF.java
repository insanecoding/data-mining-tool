package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "IDF")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "entry_id")
public class IDF implements Serializable{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entry_id")
    private Long entry_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="exp_num")
    @NonNull
    private Experiment experiment;

    @Column(name = "term", columnDefinition="TEXT")
    @NonNull
    private String term;

    @Column(name = "value", columnDefinition="TEXT")
    @NonNull
    private String value;
}
