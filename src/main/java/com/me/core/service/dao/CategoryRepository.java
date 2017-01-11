package com.me.core.service.dao;

import com.me.core.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategoryNameIn(List<String> categoryName);
    Category findByCategoryName(String categoryName);
}
