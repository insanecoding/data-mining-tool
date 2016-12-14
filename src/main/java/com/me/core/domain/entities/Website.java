package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "websites")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "websiteId")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Website implements Serializable{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "website_id")
    private Long websiteId;

    @Column(name = "url", unique = true)
    @NonNull
    private String url;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    @NonNull
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blacklist_id")
    @NonNull
    private Blacklist blacklist;

}
