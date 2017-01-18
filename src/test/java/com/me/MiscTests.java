package com.me;

import com.me.core.domain.dto.Modes;
import com.me.core.domain.dto.Types;
import com.me.core.domain.entities.*;
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
        RegularExperiment re = new RegularExperiment();
        re.setExpName("regular_1");
        DataSet ds = myDao.findDataSetByName("set_2");
        re.setDataSet(ds);
        re.setDescription("desc");
        re.setType(Types.BINOMIAL);
        re.setMode(Modes.NGRAMS);
        ExperimentParam ep = new ExperimentParam();
        ep.setFeaturesByCategory(20);
        ep.setNGramSize(5);
        re.setExperimentParam(ep);
        myDao.saveEntity(re);

        RegularExperiment re2 = new RegularExperiment();
        re2.setExpName("regular_2");
        DataSet ds2 = myDao.findDataSetByName("set_1");
        re2.setDataSet(ds2);
        re2.setDescription("desc 2");
        re2.setType(Types.BINOMIAL);
        re2.setMode(Modes.NGRAMS);
        ExperimentParam ep2 = new ExperimentParam();
        ep2.setFeaturesByCategory(10);
        ep2.setNGramSize(6);
        re2.setExperimentParam(ep2);
        myDao.saveEntity(re2);

        JoinedExperiment je = new JoinedExperiment();
        je.setExpName("joined_1");
        je.setDescription("desc 4");
        myDao.saveEntity(je);

        List<RegularExperiment> regularExperiments = myDao.findRegularByNames(Arrays.asList("regular_1",
                "regular_2", "regular_3", "regular_4"));
        List<DependentExperiment> deps = regularExperiments.stream()
                .map(elem -> new DependentExperiment(je, elem))
                .collect(Collectors.toList());
        myDao.batchSave(deps);
    }
}
