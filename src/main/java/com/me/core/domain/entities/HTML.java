package com.me.core.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "htmls")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class HTML implements Serializable {

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="website_id")
    @NonNull
    private Website website;

    @Column(name = "html", columnDefinition="TEXT")
    @NonNull
    private String html;
}
