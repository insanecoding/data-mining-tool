package com.me.core.service.dao;

import com.me.core.domain.entities.Blacklist;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.Website;

import java.io.Serializable;
import java.util.List;

public interface MyDao {
    <T extends Serializable> T saveEntity(T entity);

    <T extends Serializable> void batchSave(Iterable<T> entities) throws InterruptedException;

    void batchSaveWebsites(List<Website> websites) throws InterruptedException;

    Category trySaveCategory(Category category);

    Blacklist trySaveBlacklist(Blacklist blacklist);

    List<Category> findCategoriesByNames(List<String> categoryNames);

    <T extends Serializable> List<T> findByCategory(String whatToFind, Category category);

    List<Long> alreadyProcessedIDsFor(String mode, Category category);

    Long countDuplicates();
}