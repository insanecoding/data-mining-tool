package com.me;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class MiscTests {
    @Test
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
}
