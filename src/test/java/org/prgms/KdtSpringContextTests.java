package org.prgms;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//Spring TestContext를 사용하도록 설정
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class KdtSpringContextTests {

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("applicationContext가 생성")
    void testApplicationContext() {
        assertThat(context, notNullValue());
    }
}
