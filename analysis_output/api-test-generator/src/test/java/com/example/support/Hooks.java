package com.example.support;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Global Cucumber hooks that run before and after every scenario.
 * Uses PicoContainer to receive the shared {@link TestContext}.
 */
public class Hooks {

    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp(Scenario scenario) {
        LOG.info("Starting scenario: {}", scenario.getName());
        context.reset();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            LOG.error("Scenario FAILED: {}", scenario.getName());
            if (context.getLastResponse() != null) {
                LOG.error("Last response status: {}", context.getLastResponse().statusCode());
                LOG.error("Last response body: {}", context.getLastResponse().asString());
            }
        } else {
            LOG.info("Scenario PASSED: {}", scenario.getName());
        }
    }
}
