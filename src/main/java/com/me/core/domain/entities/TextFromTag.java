package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "text_from_tag")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "entryID")
public class TextFromTag implements Serializable, AbstractText {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entry_id")
    private Long entryID;

    @OneToOne
    @JoinColumn(name="website_id")
    @NonNull
    private Website website;

    // cascade type parameter used, because Tag entity may not exist while
    // inserting TextFromTag entity and we need to make sure it will be stored
    // properly and no exceptions occur
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="tag_id")
    @NonNull
    private Tag tag;

    @Column(name = "text", columnDefinition="TEXT")
    @NonNull
    private String text;

    @Column(name = "length")
    @NonNull
    private int length;

    @Override
    public Website getWebsite() {
        return website;
    }

    @Override
    public String getText() {
        return text;
    }
}
