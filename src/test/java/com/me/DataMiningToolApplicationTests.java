package com.me;

import com.me.core.domain.dto.State;
import com.me.core.domain.dto.States;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataMiningToolApplicationTests {


    @Test @Ignore
    public void contextLoads() {
        State state = new State("info", States.WORKING, 0);
        State state2 = new State("info", States.WORKING, 1);
        State state3 = new State("info", States.WORKING, 0);
        State state4 = null;

        assertNotEquals(state, state4);
        assertNotEquals(state, state2);
        assertEquals(state, state3);
    }
}
