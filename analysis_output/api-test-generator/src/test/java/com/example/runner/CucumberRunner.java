package com.example.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Cucumber test runner that discovers and executes all feature files.
 * Configured to produce JSON and HTML reports in target/cucumber-reports/.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.example.steps", "com.example.support"},
        plugin = {
                "pretty",
                "json:target/cucumber-reports/cucumber.json",
                "html:target/cucumber-reports/cucumber.html",
                "junit:target/cucumber-reports/cucumber-junit.xml"
        },
        tags = "not @ignore",
        monochrome = true
)
public class CucumberRunner {
    // Entry point for Cucumber JUnit runner - no code required
}
