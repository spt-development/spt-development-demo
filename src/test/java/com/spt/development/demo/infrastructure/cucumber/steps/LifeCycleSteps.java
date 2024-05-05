package com.spt.development.demo.infrastructure.cucumber.steps;

import com.spt.development.cid.CorrelationId;
import com.spt.development.demo.infrastructure.cucumber.SpringConfiguration;
import com.spt.development.demo.infrastructure.cucumber.util.DatabaseTestUtil;
import com.spt.development.test.integration.HttpTestManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class LifeCycleSteps {
    @LocalServerPort private int localServerPort;

    @Autowired private DataSource dataSource;
    @Autowired private HttpTestManager httpTestManager;

    @BeforeAll
    public static void fixtureSetUp() {
        SpringConfiguration.activeMq.start();
        SpringConfiguration.postgresDB.start();
    }

    @Before
    public void setUp(Scenario scenario) {
        LOG.info("Running scenario: '{}'", scenario.getName());

        httpTestManager.init(localServerPort);

        clearDatabase();

        CorrelationId.set("integration-test-runner-cid");
    }

    private void clearDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseTestUtil.clearDatabase(connection);
        }
        catch (SQLException ex) {
            LOG.warn("Failed to clear database", ex);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        LOG.info("Finishing scenario: '{}', with status: {}", scenario.getName(), scenario.getStatus());
    }

    @AfterAll
    public static void fixtureTearDown() {
        SpringConfiguration.postgresDB.close();
    }
}
