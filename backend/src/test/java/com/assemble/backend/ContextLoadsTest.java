package com.assemble.backend;

import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ContextLoadsTest {

    @Test
    void contextLoads() {
    }

}
