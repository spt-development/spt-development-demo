package com.spt.development.demo.cucumber;

import com.spt.development.test.integration.HttpTestManager;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.spring.CucumberTestContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringConfiguration {
    private static final String POSTGRES_TAG = "15.3";

    @ServiceConnection
    public static PostgreSQLContainer<?> postgresDB =
        new PostgreSQLContainer<>(DockerImageName.parse(PostgreSQLContainer.IMAGE).withTag(POSTGRES_TAG));

    @TestConfiguration
    public static class TestManagerConfig {

        @Bean
        @Scope(CucumberTestContext.SCOPE_CUCUMBER_GLUE)
        public HttpTestManager httpTestManager() {
            return new HttpTestManager();
        }
    }
}
