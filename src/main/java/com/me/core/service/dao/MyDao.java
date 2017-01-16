package com.me.core.service.dao;

import com.me.core.domain.dto.DictionaryParam;
import com.me.core.domain.dto.MainDataSplitParams;
import com.me.core.domain.dto.Modes;
import com.me.core.domain.entities.*;

import java.io.Serializable;
import java.util.List;

public interface MyDao {

/*
 * savers
 */
    <T extends Serializable> T saveEntity(T entity);

    <T extends Serializable> void batchSave(Iterable<T> entities, Object... param) throws InterruptedException;

    void batchSaveWebsites(List<Website> websites) throws InterruptedException;

    Category trySaveCategory(Category category);

    Blacklist trySaveBlacklist(Blacklist blacklist);

    Tag trySaveTag(Tag tag);

    Experiment trySaveExperiment(Experiment experiment);

    DataSet trySaveDataSet(DataSet dataSet);
/*
 * finders
 */
    List<Category> findCategoriesByNames(List<String> categoryNames);

    List<Tag> findTagsByNames(List<String> tags);

    <T extends Serializable> List<T> findByCategory(String whatToFind, Category category);

    List<Long> alreadyProcessedIDsFor(String mode, Category category, Object... args);

    Long countDuplicates();

    List<Website> findWebsitesWithCondition(Category category, MainDataSplitParams mdp);

    List<Experiment> findExperimentsByNames(List<String> experimentNames);

    List<ChosenWebsite> findChosenWebsites(DataSet dataSet, Category category);

    List<ChosenCategory> findCategoriesByDataSet(DataSet dataSet);

    List<? extends AbstractText> findTextsForIDs(List<Long> IDs, Category category,
                                                 Modes mode, DictionaryParam param);

    DataSet findDataSetByName(String dataSetName);
}