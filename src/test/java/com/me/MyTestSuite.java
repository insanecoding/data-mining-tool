package com.me;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Suite.class)
@SpringBootTest
@ActiveProfiles("dev")
@Suite.SuiteClasses({
        DbConnectionTests.class
})
public class MyTestSuite {
}
