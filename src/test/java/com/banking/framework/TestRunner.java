package com.banking.framework;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.banking.framework.steps",
    plugin = {},
    monochrome = true
)
public class TestRunner {
}
