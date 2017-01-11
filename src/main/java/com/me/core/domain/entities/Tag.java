package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tags")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "tagID")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private Long tagID;

    @Column(name = "tag_name", unique = true, columnDefinition="TEXT")
    @NonNull
    private String tagName;
}
