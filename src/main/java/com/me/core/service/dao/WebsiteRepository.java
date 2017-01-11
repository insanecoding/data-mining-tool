package com.me.core.service.dao;

import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.Website;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebsiteRepository extends JpaRepository <Website, Long> {
    List<Website> findByCategoryIn(List<Category> category);
    Long countByUrl(String url);
}
