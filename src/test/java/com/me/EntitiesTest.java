package com.me;

import com.me.core.domain.entities.*;
import com.me.core.service.dao.MyDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class EntitiesTest {

    @Autowired
    private MyDao dao;

    @Test
    @Ignore
    public void test() throws Exception {
        Website website = new Website("website", new Category("category"),
                new Blacklist("name", "url", new Date()));
        NGrams nGrams = new NGrams( website, 4, "bla bla");
        nGrams.setEntryID(-1L);
        NGrams nGrams2 = new NGrams( website, 4, "bla bla");
        nGrams2.setEntryID(null);

        myAssertEquals(nGrams, nGrams2);
        nGrams.setNGramSize(3);
        myAssertNotEquals(nGrams, nGrams2);

        Tag tag = new Tag("tag");
        Tag tag2 = new Tag("tag");
        myAssertEquals(tag, tag2);
        tag2.setTagName("another tag");
        myAssertNotEquals(tag, tag2);

        TextFromTag textFromTag = new TextFromTag(website, tag, "text", 42);
        TextFromTag textFromTag2 = new TextFromTag(website, tag, "text", 42);
        myAssertEquals(textFromTag, textFromTag2);

        textFromTag.setTag(tag2);
        myAssertNotEquals(textFromTag, textFromTag2);

        TextMain textMain = new TextMain(website, "text", "en", 42);
        TextMain textMain2 = new TextMain(website, "text", "en", 42);

        myAssertEquals(textMain, textMain2);
        textMain.setLength(128);
        myAssertNotEquals(textMain, textMain2);

    }

    private void myAssertNotEquals(Object ob1, Object ob2) {
        assertNotEquals(ob1, ob2);
        assertNotEquals(ob1.hashCode(), ob2.hashCode());
    }

    private void myAssertEquals(Object ob1, Object ob2) {
        assertEquals(ob1, ob2);
        assertEquals(ob1.hashCode(), ob2.hashCode());
    }

    @Test
    public void name() throws Exception {
        Category category1 = new Category("category 1");
        Category category2 = new Category("category 2");
        Blacklist blacklist1 = new Blacklist("blacklist1", "url1", new Date());
        Blacklist blacklist2 = new Blacklist("blacklist2", "url2", new Date());

        List<Website> websitesLearn = new ArrayList<>(
                Arrays.asList(
                        new Website("website1", category1, blacklist1),
                        new Website("website2", category1, blacklist1),
                        new Website("website3", category1, blacklist1)
                )
        );
        List<Website> websitesTest = new ArrayList<>(
                Arrays.asList(
                        new Website("website4", category2, blacklist2),
                        new Website("website5", category2, blacklist2)
                )
        );


        List<Category> categoryList = Arrays.asList(
                category1,
                category2,
                new Category("category 3"),
                new Category("category 4")
        );
        dao.batchSave(categoryList);
        dao.batchSave(Arrays.asList(blacklist1, blacklist2));
        dao.batchSave(websitesLearn);
        dao.batchSave(websitesTest);

        DataSet dataSet = new DataSet("name", "desc", 0.8);
        dao.batchSave(Collections.singletonList(dataSet));
    }
}
