@smoke @regression
Feature: AC3 - Identify the warmest Australian capital city
  As a data analyst
  I want to programmatically identify which Australian capital city is currently the warmest
  So that I can make data-driven travel or logistics decisions

  Background:
    Given The WeatherBit API is available

  @ac3
  Scenario: Identify the warmest Australian capital city from all capitals
    Given the list of Australian capital cities:
      | city      | state                        |
      | Sydney    | New South Wales              |
      | Melbourne | Victoria                     |
      | Brisbane  | Queensland                   |
      | Perth     | Western Australia            |
      | Adelaide  | South Australia              |
      | Hobart    | Tasmania                     |
      | Darwin    | Northern Territory           |
      | Canberra  | Australian Capital Territory |
    When I retrieve current temperature for each Australian capital city
    Then I should be able to identify the warmest Australian capital city
    And the warmest city result should be logged with its temperature