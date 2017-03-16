package com.me;

import com.me.core.domain.entities.Blacklist;
import com.me.core.domain.entities.Category;
import com.me.core.domain.entities.HTML;
import com.me.core.domain.entities.Website;
import com.me.core.service.dao.MyDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")
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
    public void testFindWebsitesByCategory() {
        List<Category> categories =
                dao.findCategoriesByNames(Arrays.asList("news", "vacation", "medical"));
        categories.forEach(category -> {
            List<Website> websites = dao.findByCategory("websites", category);
            assertThat(websites.size()).isNotEqualTo(0);
        });
    }

    @Test @Ignore
    public void testFindHTMLsByCategory() throws Exception {
        Category category =
                dao.findCategoriesByNames(Collections.singletonList("news")).get(0);
        List<HTML> htmls = dao.findByCategory("htmls", category);
        assertThat(htmls.size()).isNotEqualTo(0);
    }

    @Test @Ignore
    public void testAlreadyProcessedTextMain() throws Exception {
        Category category =
                dao.findCategoriesByNames(Collections.singletonList("news")).get(0);
        List<Long> ids = dao.alreadyProcessedIDsFor("texts_main", category);
        assertThat(ids.size()).isEqualTo(0);
    }
}
