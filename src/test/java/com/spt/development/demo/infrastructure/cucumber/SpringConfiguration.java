package com.spt.development.demo.infrastructure.cucumber;

import com.spt.development.test.integration.HttpTestManager;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.spring.CucumberTestContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.grafana.LgtmStackContainer;
import org.testcontainers.utility.DockerImageName;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringConfiguration {
    private static final String ACTIVEMQ_IMAGE = "apache/activemq-classic";
    private static final String ACTIVEMQ_TAG = "6.1.4";

    private static final String LGTM_IMAGE_NAME = "grafana/otel-lgtm";
    private static final String LGTM_TAG = "0.8.1";

    private static final String POSTGRES_TAG = "17.2-alpine3.20";

    @ServiceConnection
    public static ActiveMQContainer activeMq =
        new ActiveMQContainer(DockerImageName.parse(ACTIVEMQ_IMAGE).withTag(ACTIVEMQ_TAG));

    @ServiceConnection
    public static LgtmStackContainer lgtmStackContainer =
        new LgtmStackContainer(DockerImageName.parse(LGTM_IMAGE_NAME).withTag(LGTM_TAG));

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
