package com.example.manure.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CucumberHooks provides setup and teardown logic that runs before and after each scenario.
 */
public class CucumberHooks {

    private static final Logger LOG = LoggerFactory.getLogger(CucumberHooks.class);

    private final TestContext context;

    public CucumberHooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        LOG.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            LOG.error("Scenario FAILED: {}", scenario.getName());
            if (context.getResponse() != null) {
                LOG.error("Last response status: {}", context.getResponse().getStatusCode());
                LOG.error("Last response body: {}", context.getResponse().getBody().asString());
            }
        } else {
            LOG.info("Scenario PASSED: {}", scenario.getName());
        }
    }
}
