package com.qantas.weather.hooks;

import io.cucumber.java.*;
import org.junit.AssumptionViolatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    @BeforeAll
    public static void suiteSetup(){
        log.info("======================================");
        log.info("  Qantas Loyalty - Weather API Tests  ");
        log.info("  Suite Starting...                   ");
        log.info("======================================");
    }

    @Before
    public void setup(Scenario scenario){
        log.info("Starting scenario:  [{}]", scenario.getName());
    }



    @After
    public void tearDown(Scenario scenario){
        if(scenario.isFailed()){
            log.error("---- FAILED Scenario: [{}] -----", scenario.getName());
            log.error(" Check the Serenity HTML report for full request/response details");
        }
        else{
            log.info("----- PASSED Scenario: [{}] -----", scenario.getName());
        }
    }

    @AfterAll
    public static void suiteTearDown(){
        log.info("======================================");
        log.info("  Suite Completed. Open the report at  ");
        log.info("  target/site/serenity/index.html      ");
        log.info("======================================");
    }
}
