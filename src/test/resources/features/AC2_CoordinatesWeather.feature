@smoke @regression
Feature: AC2 - Retrieve weather data for specific coordinates
  As a traveller
  I want to ensure the API returns valid data for specific coordinates
  So that I can get accurate weather information for any location
  Background:
    Given The WeatherBit API is available

  @ac2
  Scenario: Retrieve weather for Sydney CBD coordinates
    When I request current weather for coordinates latitude "-33.865143" and longitude "151.209900"
    Then the response status code should be 200
    And the response should contain temperature data
    And the response body should have a valid JSON structure
    And the response latitude should be approximately "-33.865143"
    And the response longitude should be approximately "151.209900"

  @ac2
  Scenario: Verify response schema for coordinate-based weather request
    When I request current weather for coordinates latitude "-33.865143" and longitude "151.209900"
    Then the response status code should be 200
    And the response should contain a non-empty weather description
    And the response should contain wind speed data
    And the response should contain humidity data
    And the response should contain observation datetime