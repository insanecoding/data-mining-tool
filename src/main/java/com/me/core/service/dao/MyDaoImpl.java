package com.me.core.service.dao;

import com.me.common.StoppableObservable;
import com.me.core.domain.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Repository("myDaoImpl")
@Transactional
@Slf4j
public class MyDaoImpl extends StoppableObservable implements MyDao {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private final SessionFactory sessionFactory;
    @Lazy
    private final JdbcTemplate jdbcTemplate;
    @Lazy
    private final CategoryRepository categoryRepository;
    @Lazy
    private final WebsiteRepository websiteRepository;

    @Autowired
    public MyDaoImpl(SessionFactory sessionFactory,
                     @Qualifier("categoryRepository") CategoryRepository categoryRepository,
                     @Qualifier("websiteRepository") WebsiteRepository websiteRepository,
                     JdbcTemplate jdbcTemplate) {
        this.sessionFactory = sessionFactory;
        this.categoryRepository = categoryRepository;
        this.websiteRepository = websiteRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void batchSaveWebsites(List<Website> websites) throws InterruptedException {
        Stream<Website> websiteStream = websites.stream().filter(this::isUnique);
        batchSave(websiteStream::iterator);
    }

    private boolean isUnique(Website website) {
        // check whether website is unique
        Long count = websiteRepository.countByUrl(website.getUrl());
        // if count == 0 (the entry hasn't existed yet) then it is unique so return true
        // otherwise it already exists so is duplicate, that's why return false
        return count == 0;
    }

    @Override
    public <T extends Serializable> void batchSave(Iterable<T> entities)
            throws InterruptedException {
        Session session = sessionFactory.getCurrentSession();
        int successAddCounter = 0;

        for (T entity : entities) {
            session.save(entity);
            successAddCounter++;
            super.checkCancel();

            if (successAddCounter % batchSize == 0) {
                session.flush();
                session.clear();
            }
        }
    }

    @Override
    public Category trySaveCategory(Category category) {
        // check whether entity exists
        Category result = findCategory(category.getCategoryName());
        // if entity hasn't existed yet
        if (result == null) {
            // save and return
            return saveEntity(category);
        } else {
            // result is not null - entity already exists
            // simply return
            return result;
        }
    }

    @Transactional(readOnly = true)
    @SuppressWarnings(value = "unchecked")
    private Category findCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    @Override
    public Blacklist trySaveBlacklist(Blacklist blacklist) {
        // check whether entity exists
        Blacklist result = findBlacklist(blacklist.getBlacklistName());
        // if entity hasn't existed yet
        if (result == null) {
            // save and return
            return saveEntity(blacklist);
        } else {
            // result is not null - entity already exists
            // simply return
            return result;
        }
    }

    @Override
    public Tag trySaveTag(Tag tag) {
       Tag newTag = findTag(tag.getTagName());
       return (newTag == null) ? saveEntity(tag) : newTag;
    }

    private Tag findTag(String tagName) {
        return (Tag) sessionFactory.getCurrentSession()
                .createQuery("from Tag t where t.tagName = :tagName")
                .setParameter("tagName", tagName).uniqueResult();
    }

    private Blacklist findBlacklist(String blacklistName) {
        return (Blacklist) sessionFactory.getCurrentSession()
                .createQuery("from Blacklist b where b.blacklistName = :category")
                .setParameter("category", blacklistName).uniqueResult();
    }

    @Override
    public <T extends Serializable> T saveEntity(T entity) {
        sessionFactory.getCurrentSession().save(entity);
        return entity;
    }

    @Override
    public List<Category> findCategoriesByNames(List<String> categoryNames) {
        return categoryRepository.findByCategoryNameIn(categoryNames);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tag> findTagsByNames(List<String> targetTags) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Tag t where t.tagName in (:targetTags)")
                .setParameterList("targetTags", targetTags).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Serializable> List<T> findByCategory(String whatToFind, Category category) {
        switch (whatToFind) {
            case "websites":
                List<Category> categoryList = Collections.singletonList(category);
                return (List<T>) websiteRepository.findByCategoryIn(categoryList);
            case "htmls":
                return (List<T>) findHTMLByCategory(category);
            default:
                throw new IllegalArgumentException("Illegal argument 'whatToFind'");
        }
    }
    
    @SuppressWarnings("unchecked")
    private List<HTML> findHTMLByCategory(Category category) {
        return sessionFactory.getCurrentSession()
                .createQuery("from HTML h where h.website.category = :category")
                .setParameter("category", category).list();
    }

    @Override
    public List<Long> alreadyProcessedIDsFor(String mode, Category category,
                                             Object... args) {
        switch (mode) {
            case "htmls":
                return alreadyProcessedHtmlIDs(category);
            case "texts_main":
                return alreadyProcessedTextMainIDs(category);
            case "texts_from_tags": {
                Tag tag = (Tag) args[0];
                return alreadyProcessedTextFromTagIDs(category, tag);
            }
            case "ngrams": {
                int nGramSize = (int) args[0];
                return alreadyProcessedNGramIDs(category, nGramSize);
            }
            case "tags_in_page":
                return alreadyProcessedTagsInPageIDs(category);
            case "tag_count":
                return alreadyProcessedTagCountIDs(category);
            default:
                throw new IllegalArgumentException("Illegal argument 'mode'");
        }
    }

    @SuppressWarnings("unchecked")
    private List<Long> alreadyProcessedTagCountIDs(Category category) {
        return sessionFactory.getCurrentSession()
                .createQuery("select tc.website.websiteId " +
                        " from TagCount tc where tc.website.category in :category ")
                .setParameter("category", category)
                .list();
    }

    @SuppressWarnings("unchecked")
    private List<Long> alreadyProcessedTagsInPageIDs(Category category) {
        return sessionFactory.getCurrentSession()
                .createQuery("select distinct tip.website.websiteId " +
                        " from TagsInPage tip where tip.website.category in :category ")
                .setParameter("category", category)
                .list();
    }

    @SuppressWarnings("unchecked")
    private List<Long> alreadyProcessedNGramIDs(Category category, int nGramSize) {
        return sessionFactory.getCurrentSession()
                .createQuery("select ng.website.websiteId " +
                        " from NGrams ng where ng.website.category in :category " +
                        " and ng.nGramSize in :nGramSize")
                .setParameter("category", category)
                .setParameter("nGramSize", nGramSize)
                .list();
    }

    @SuppressWarnings("unchecked")
    private List<Long> alreadyProcessedTextFromTagIDs(Category category, Tag tag) {
        return sessionFactory.getCurrentSession()
                .createQuery("select tft.website.websiteId " +
                        " from TextFromTag tft where tft.website.category in :category " +
                        " and tft.tag in :tag")
                .setParameter("category", category)
                .setParameter("tag", tag)
                .list();
    }

    @SuppressWarnings("unchecked")
    private List<Long> alreadyProcessedTextMainIDs(Category category) {
        return sessionFactory.getCurrentSession()
                .createQuery("select tm.website.websiteId " +
                        " from TextMain tm where tm.website.category in :category")
                .setParameter("category", category).list();
    }

    @SuppressWarnings("unchecked")
    private List<Long> alreadyProcessedHtmlIDs(Category category) {
        return sessionFactory.getCurrentSession()
                .createQuery("select c.website.websiteId " +
                        " from Connect c where c.website.category in :category")
                .setParameter("category", category).list();
    }

    @Override
    public Long countDuplicates() {
        String sql = "SELECT coalesce(sum(dupes.c) - count(dupes.c), 0) " +
                "FROM (SELECT count(*) AS c " +
                "      FROM websites " +
                "      GROUP BY url " +
                "      HAVING count(*) > 1 " +
                "     ) AS dupes";
        return jdbcTemplate.queryForObject(
                sql, new Object[] {}, Long.class);
    }
}
