@smoke @regression
Feature: AC4 - Find the current coldest US State
  As a logistics manager
  I want to find the current coldest US State by injecting a list of states via a metadata input file
  So that I can optimise logistics and supply chain operations

  Background:
    Given The WeatherBit API is available

  @ac4
  Scenario: Identify the coldest US state using the metadata input file
    Given the list of US states is loaded from the metadata file "testdata/us_states.json"
    When I retrieve current temperature for the capital city of each US state
    Then I should be able to identify the coldest US state
    And the coldest state result should be logged with its capital city and temperature