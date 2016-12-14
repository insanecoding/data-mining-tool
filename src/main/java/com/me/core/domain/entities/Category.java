package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "categories")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "categoryId")
public class Category implements Serializable{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", unique = true)
    @NonNull
    private String categoryName;
}
