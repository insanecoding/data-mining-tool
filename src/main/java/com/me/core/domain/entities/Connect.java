package com.me.core.domain.entities;

import com.me.core.service.download.DownloadResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "connects")

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Connect implements Serializable{

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="website_id")
    @NonNull
    private Website website;

    @Column(name = "duration")
    @NonNull
    private long duration;

    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    @NonNull
    private DownloadResult result;
}
