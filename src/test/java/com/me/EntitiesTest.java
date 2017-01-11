package com.me;

import com.me.core.domain.entities.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class EntitiesTest {

    @Test
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
}
