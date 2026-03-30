package com.example.manure.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * CucumberTestRunner is the entry point for running the full API test suite.
 * Run with: mvn test
 * Run smoke tests only: mvn test -Dcucumber.filter.tags="@smoke"
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {
        "com.example.manure.steps",
        "com.example.manure.support"
    },
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber.html",
        "json:target/cucumber-reports/cucumber.json",
        "junit:target/cucumber-reports/cucumber.xml"
    },
    tags = "not @wip",
    monochrome = true
)
public class CucumberTestRunner {
    // Intentionally empty - Cucumber manages test execution
}
