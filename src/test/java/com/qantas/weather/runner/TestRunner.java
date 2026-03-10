package com.qantas.weather.runner;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class TestRunner {
    // No code needed — JUnit 5 Suite uses annotations only.
    // Cucumber settings live in src/test/resources/junit-platform.properties
}
