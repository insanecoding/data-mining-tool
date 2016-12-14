package com.me.core.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "blacklists")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "blacklistId")
public class Blacklist implements Serializable{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "blacklist_id")
    private Long blacklistId;

    @Column(name = "blacklist_name", unique = true)
    @NonNull
    private String blacklistName;

    @Column(name = "blacklist_url")
    @NonNull
    private String url;

    @Temporal(TemporalType.DATE)
    @Column(name = "import_date")
    @NonNull
    private Date importDate;

}
