package com.spt.development.demo.cucumber;

import com.spt.development.test.integration.HttpTestManager;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.spring.CucumberTestContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringConfiguration {
    private static final String POSTGRES_TAG = "16.1";

    // This is the image that Spring Boot provides ServiceConnection support for:
    // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.docker-compose.service-connections
    private static final String ACTIVEMQ_IMAGE = "symptoma/activemq";
    private static final String ACTIVEMQ_TAG = "5.18.3";
    private static final int ACTIVEMQ_OPEN_WIRE_PORT = 61_616;

    @ServiceConnection
    public static PostgreSQLContainer<?> postgresDB =
        new PostgreSQLContainer<>(DockerImageName.parse(PostgreSQLContainer.IMAGE).withTag(POSTGRES_TAG));

    @ServiceConnection
    public static GenericContainer<?> activeMq =
        new GenericContainer<>(DockerImageName.parse(ACTIVEMQ_IMAGE).withTag(ACTIVEMQ_TAG))
            .withExposedPorts(ACTIVEMQ_OPEN_WIRE_PORT);

    @TestConfiguration
    public static class TestManagerConfig {

        @Bean
        @Scope(CucumberTestContext.SCOPE_CUCUMBER_GLUE)
        public HttpTestManager httpTestManager() {
            return new HttpTestManager();
        }
    }
}
