package com.me.core.service.dao;

import com.me.core.domain.entities.Blacklist;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.Tag;
import com.me.core.domain.entities.Website;

import java.io.Serializable;
import java.util.List;

public interface MyDao {

/*
 * savers
 */
    <T extends Serializable> T saveEntity(T entity);

    <T extends Serializable> void batchSave(Iterable<T> entities) throws InterruptedException;

    void batchSaveWebsites(List<Website> websites) throws InterruptedException;

    Category trySaveCategory(Category category);

    Blacklist trySaveBlacklist(Blacklist blacklist);

    Tag trySaveTag(Tag tag);
/*
 * finders
 */
    List<Category> findCategoriesByNames(List<String> categoryNames);

    List<Tag> findTagsByNames(List<String> tags);

    <T extends Serializable> List<T> findByCategory(String whatToFind, Category category);

    List<Long> alreadyProcessedIDsFor(String mode, Category category, Object... args);

    Long countDuplicates();
}