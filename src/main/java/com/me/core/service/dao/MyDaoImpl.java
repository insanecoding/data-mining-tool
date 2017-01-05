package com.me.core.service.dao;

import com.me.common.StoppableObservable;
import com.me.core.domain.entities.Blacklist;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.Website;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    private final CategoryRepo categoryRepo;
    private final WebsiteRepo websiteRepo;
    private final JdbcTemplate jdbcTemplate;
    private final BlacklistRepo blacklistRepo;

    @Autowired
    public MyDaoImpl(SessionFactory sessionFactory,
                     @Qualifier("categoryRepo") CategoryRepo categoryRepo,
                     @Qualifier("websiteRepo") WebsiteRepo websiteRepo,
                     JdbcTemplate jdbcTemplate, BlacklistRepo blacklistRepo) {
        this.sessionFactory = sessionFactory;
        this.categoryRepo = categoryRepo;
        this.websiteRepo = websiteRepo;
        this.jdbcTemplate = jdbcTemplate;
        this.blacklistRepo = blacklistRepo;
    }

    @Override
    public <T extends Serializable> T saveEntity(T entity) {
        sessionFactory.getCurrentSession().save(entity);
        return entity;
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
    public void batchSaveWebsites(List<Website> websites) throws InterruptedException {
//        Session session = sessionFactory.getCurrentSession();
        Stream<Website> websiteStream = websites.stream()
                .filter(this::isUnique);
        batchSave(websiteStream::iterator);
    }

    @Override
    public Category trySaveCategory(Category category) {
        // check whether entity exists
        Category result = findCategory(category);
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

    @Override
    public Blacklist trySaveBlacklist(Blacklist blacklist) {
        // check whether entity exists
        Blacklist result = blacklistRepo.findByBlacklistName(blacklist.getBlacklistName());
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
    @Transactional(readOnly = true)
    @SuppressWarnings(value = "unchecked")
    public Category findCategory(Category category) {
        return categoryRepo.findByCategoryName(category.getCategoryName());
    }

    @Override
    public List<Category> findCategoriesByNames(List<String> categoryNames) {
        return categoryRepo.findByCategoryNameIn(categoryNames);
    }

    @Override
    public List<Website> findWebsitesByCategory(Category category) {
        return websiteRepo.findByCategoryIn(Collections.singletonList(category));
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

    private boolean isUnique(Website website) {
        // check whether website is unique
        Long count = websiteRepo.countByUrl(website.getUrl());
        // if count == 0 (the entry hasn't existed yet) then it is unique so return true
        // otherwise it already exists so is duplicate, that's why return false
        return count == 0;
    }
//    private boolean isUnique(Session session, Website website) {
//         // check whether website is unique
//        Long count = websiteRepo.countByUrl(website.getUrl());
//        // if count == 0 (the entry hasn't existed yet) then it is unique so return true
//        // otherwise it already exists so is duplicate, that's why return false
//        return count == 0;
//    }
//
//    private Long countWebsites(Session session, Website website) {
//        Query query = session.createQuery(
//                "select count(*) from Website websites " +
//                        "where websites.url = :url"
//        ).setParameter("url", website.getUrl());
//        return (Long)query.uniqueResult();
//    }
}
