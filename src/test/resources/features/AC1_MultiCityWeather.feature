@smoke @regression
Feature: AC1 - Retrieve current weather data for multiple major international cities
  As a frequent flyer
  I want to verify I can retrieve current weather data for a list of multiple major international cities
  So that I can plan my travel accordingly

  Background:
    Given The WeatherBit API is available

  @ac1
  Scenario: Successfully retrieve weather data for all major international cities
    When I request current weather for the following cities:
      | city          | country |
      | Sydney        | AU      |
      | London        | GB      |
      | New York      | US      |
      | Tokyo         | JP      |
      | Paris         | FR      |
      | Dubai         | AE      |
      | Singapore     | SG      |
      | Los Angeles   | US      |
    Then Each City should return a 200 status code
    And Each City response should contain a valid temperature in Celsius
    And Each City response should contain a non-empty city name
    And Each City response should contain weather description

  Scenario Outline: Retrieve weather for individual international city "<city>"
    When I request current weather for city "<city>" in country "<country>"
    Then the response status code should be 200
    And the response should contain temperature data
    And the response should contain the city name "<city>"

    Examples:
      | city        | country |
      | Sydney      | AU      |
      | London      | GB      |
      | New York    | US      |
      | Tokyo       | JP      |
      | Paris       | FR      |

# ================================================================
  # ERROR HANDLING SCENARIOS
  # ================================================================

  @ac1 @error-handling @http400
  Scenario: HTTP 400 — Missing location parameters
    When I request current weather with no location parameters
    Then the response status code should be 400
    And the response body should contain an error message

#  @ac1 @error-handling @http400 @ignore
#  Scenario Outline: HTTP 400 — Bad Request for invalid city input "<description>"
#    When I request current weather for city <city> in country <country>
#    Then the response status code should be <expectedStatusCode>
#    And the response body should contain an error message
#    Examples:
#      | city     | country     | expectedStatusCode |
#      | "<city>" | "<country>" | 400                |

  @ac1 @error-handling @http400
  Scenario: HTTP 400 — Out-of-range coordinates
    When I request current weather for coordinates latitude "999" and longitude "999"
    Then the response status code should be 400
    And the response body should contain an error message

  @ac1 @error-handling @http403
  Scenario: HTTP 403 — Missing API key
    When I request current weather for city "Sydney" in country "AU" without an API key
    Then the response status code should be 403
    And the response body should contain an authentication error message

  @ac1 @error-handling @http403
  Scenario: HTTP 403 — Invalid API key
    When I request current weather for city "Sydney" in country "AU" with an invalid API key
    Then the response status code should be 403
    And the response body should contain an authentication error message