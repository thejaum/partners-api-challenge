package com.thejaum.challenge.partner.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={com.thejaum.challenge.partner.DataApplication.class})
@ActiveProfiles("test")
public class BaseTest {

    @Test
    public void init(){
        assert(true);
    }
}
