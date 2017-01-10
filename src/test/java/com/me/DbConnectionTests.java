package com.me;

import com.me.core.domain.entities.Blacklist;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.Website;
import com.me.core.service.dao.DbCreationUtility;
import com.me.core.service.dao.MyDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class DbConnectionTests {

    @Autowired
    private MyDao dao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long countWebsites() {
        String sql = "SELECT count(*) FROM websites";
        return jdbcTemplate.queryForObject(
                sql, new Object[]{}, Long.class);
    }

    @Test @Ignore
    public void testSaveToDb() throws InterruptedException {


        Blacklist blacklist = new Blacklist("my blacklist 1", "someblacklist", new Date());

        for (int i = 0; i < 100; i++) {
            Category category = new Category("my category" + i);
            List<Website> websites = new LinkedList<>();
            for (int j = 0; j < 1000; j++) {
                Website website = new Website("my website" + i + "_" + j, category, blacklist);
                websites.add(website);
            }
            dao.batchSave(websites);
        }
        assertThat(dao.countDuplicates()).isEqualTo(0);
        assertThat(countWebsites()).isEqualTo(100_000);
    }

    @Test @Ignore
    public void testConnection() {
        assertTrue(DbCreationUtility.dbExists("jdbc:postgresql://localhost:5432/Website_Classification",
                "postgres", "postgresql"));
        assertFalse(DbCreationUtility.dbExists("jdbc:postgresql://localhost:5433/Website_Classification",
                "postgres", "postgresql"));
        assertFalse(DbCreationUtility.dbExists("jdbc:postgresql://dggdgd:34355/dgdggd",
                "zfxcxc", "postgrsffssfsfesql"));
    }

    @Test
    public void testCreateDb() {

    }
}
