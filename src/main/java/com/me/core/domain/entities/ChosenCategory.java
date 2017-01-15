package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "chosen_categories")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "entry_id")
public class ChosenCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "entry_id")
    private Long entry_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dataset_id")
    @NonNull
    private DataSet dataSet;

    @OneToOne
    @JoinColumn(name = "category_id")
    @NonNull
    private Category category;
}
