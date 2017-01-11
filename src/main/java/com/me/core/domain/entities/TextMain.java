package com.me.core.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "text_main")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TextMain implements Serializable, AbstractText {

    @Id
    @OneToOne
    @JoinColumn(name="website_id")
    @NonNull
    private Website website;

    @Column(name = "text", columnDefinition="TEXT")
    @NonNull
    private String text;

    @Column(name = "lang")
    @NonNull
    private String lang;

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
