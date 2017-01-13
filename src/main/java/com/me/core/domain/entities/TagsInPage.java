package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tags_in_page")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "entryId")
public class TagsInPage implements Serializable{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entry_id")
    private Long entryId;

    @OneToOne
    @JoinColumn(name="website_id")
    @NonNull
    private Website website;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="tag_id")
    @NonNull
    private Tag tag;

    @Column(name = "occurr_number")
    @NonNull
    private int occurrencesNumber;
}
