package com.me;

import com.me.core.domain.dto.Modes;
import com.me.core.domain.entities.DependentExperiment;
import com.me.core.domain.entities.Experiment;
import com.me.core.domain.entities.Tag;
import com.me.core.service.dao.MyDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")
public class MiscTests {

    @Autowired
    private MyDao myDao;

    @Test @Ignore
    public void testRegex() throws Exception {
        String s1 = "";
        String s2 = " ";
        String s3 = "   ";
        String s4 = "    ";
        String s5 = " What a nice day!";
        String s6 = "  What a nice day!";
        String regex = "(^$|\\s+)";

        assertTrue(s1.matches(regex));
        assertTrue(s2.matches(regex));
        assertTrue(s3.matches(regex));
        assertTrue(s4.matches(regex));
        assertFalse(s5.matches(regex));
        assertFalse(s6.matches(regex));
    }

    @Test @Ignore
    public void testFindTags() throws Exception {
        List<Tag> tags =
                Arrays.asList(new Tag("h1"), new Tag("h2"), new Tag("h3"));

        tags.forEach(myDao::saveEntity);

        List<String> tagNames = tags.stream().map(Tag::getTagName)
                .collect(Collectors.toList());
        List<Tag> result = myDao.findTagsByNames(tagNames);
        assertThat(result).isNotEmpty();

    }

    @Test
    public void testMe() throws Exception {
        Experiment owner =
                new Experiment("union_1", "united 1", Modes.JOIN);
        myDao.saveEntity(owner);

        List<Experiment> experiments =
                myDao.findExperimentsByNames(Arrays.asList("exp_7", "exp_8", "exp_10", "exp_13"));
        List<DependentExperiment> deps = experiments.stream()
                .map(elem -> new DependentExperiment(owner, elem))
                .collect(Collectors.toList());
        myDao.batchSave(deps);

        List<DependentExperiment> dependencies = myDao.findDependencies(owner);
        int i = 0;
    }
}
