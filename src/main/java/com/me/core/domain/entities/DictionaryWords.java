package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "dictionary_words")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "entry_id")
public class DictionaryWords implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entry_id")
    private Long entry_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="experiment_num")
    @NonNull
    private Experiment experiment;

    @Column(name = "dict_word", columnDefinition="TEXT")
    @NonNull
    private String word;
}
