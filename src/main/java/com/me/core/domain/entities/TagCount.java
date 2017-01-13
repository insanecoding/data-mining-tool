package com.me.core.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "website_tag_count")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TagCount implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name="website_id")
    @NonNull
    private Website website;

    @Column(name = "tag_count")
    @NonNull
    private int count;
}
