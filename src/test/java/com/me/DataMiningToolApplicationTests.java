package com.me;

import com.me.domain.State;
import com.me.domain.States;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataMiningToolApplicationTests {

    @Test
    public void contextLoads() {
        State state = new State("info", States.WORKING, 0);
        State state2 = new State("info", States.WORKING, 0);
        State state3 = new State("info", States.WORKING, 0);
        State state4 = null;

        assertNotEquals(state, state4);
        assertNotEquals(state, state2);
        assertEquals(state, state3);
    }
}
