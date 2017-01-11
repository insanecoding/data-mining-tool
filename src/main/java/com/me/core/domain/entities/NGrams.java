package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "n_grams")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "entryID")
public class NGrams implements Serializable, AbstractText {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entry_id")
    private Long entryID;

    @OneToOne
    @JoinColumn(name="website_id")
    @NonNull
    private Website website;

    @Column(name = "n_gram_size")
    @NonNull
    private int nGramSize;

    @Column(name = "n_grams", columnDefinition="TEXT")
    @NonNull
    private String nGrams;

    @Override
    public Website getWebsite() {
        return website;
    }

    @Override
    public String getText() {
        return nGrams;
    }
}
