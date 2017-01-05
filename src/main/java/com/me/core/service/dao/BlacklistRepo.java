package com.me.core.service.dao;

import com.me.core.domain.entities.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepo extends JpaRepository <Blacklist, Long> {
    Blacklist findByBlacklistName(String blacklistName);
}
