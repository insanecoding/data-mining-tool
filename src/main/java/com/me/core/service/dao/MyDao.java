package com.me.core.service.dao;

import com.me.core.domain.dto.MainDataSplitParams;
import com.me.core.domain.dto.Modes;
import com.me.core.domain.entities.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    List<ChosenWebsite> findChosenWebsites(DataSet dataSet);

    List<ChosenCategory> findCategoriesByDataSet(DataSet dataSet);

    List<? extends AbstractText> findTextsForIDs(List<Long> IDs, Category category,
                                                 Modes mode, ExperimentParam param);

    DataSet findDataSetByName(String dataSetName);

    List<DictionaryWords> findDictionaryWords(Experiment experiment);

    List<AmlFile> findAMLByExperiment(Experiment experiment);

    List<DatFile> findDatFilesForExperiment(Experiment experiment, List<Long> subsetIDs);

    List<Long> findIDsForSubset(Experiment experiment, boolean isLearn);

    List<TagsInPage> findTagsInPage(Website website);

    List<String> findTopTags(Experiment experiment);

    Map<String, Integer> findTagMaxCount(List<String> dictionaryTags,
                                         List<Long> chosenWebsiteIDs);

    Map<String,Integer> findTagInPageCount(Website website);

    long countChosenWebsites(Experiment experiment);
}