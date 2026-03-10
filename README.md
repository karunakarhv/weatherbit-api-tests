# Qantas Loyalty — Weather API Test Automation

---

## Prerequisites

| Tool               | Version | Install                                                  |
|--------------------|---------|----------------------------------------------------------|
| Java JDK           | 11+     | `brew install openjdk@11`                                |
| Maven              | 3.8+    | `brew install maven`                                     |
| Weatherbit API key | free    | [Sign up here](https://www.weatherbit.io/account/create) |

---

## Running the Tests

### Run all tests
```bash
mvn clean verify -Dapi.key=YOUR_WEATHERBIT_API_KEY
```

### Run a specific acceptance criterion
```bash
# AC1 only
mvn clean verify -Dapi.key=YOUR_KEY -Dcucumber.filter.tags="@ac1"

# AC2 only
mvn clean verify -Dapi.key=YOUR_KEY -Dcucumber.filter.tags="@ac2"

# Smoke tests only
mvn clean verify -Dapi.key=YOUR_KEY -Dcucumber.filter.tags="@smoke"
```

### Using an environment variable instead of a flag
```bash
export WEATHERBIT_API_KEY=YOUR_KEY
mvn clean verify
```
---

## Viewing the Report

After a successful run, open:
```
target/site/serenity/index.html
```
The Serenity HTML report shows:
- Scenario pass/fail status
- Full request URL, headers, and body for every API call
- Full response body and status code

---

## Acceptance Criteria Covered

| AC  | Description                                                | Feature File                      | Tags                    |
|-----|:-----------------------------------------------------------|-----------------------------------|-------------------------|
| AC1 | Retrieve weather for multiple major international cities   | AC1_MultiCityWeather.feature      | @ac1 @smoke @regression |
| AC2 | Validate response for coordinates (-33.865143, 151.209900) | AC2_CoordinatesWeather.feature    | @ac2 @smoke @regression |
| AC3 | Identify warmest Australian capital city                   | AC3_WarmestAustralianCity.feature | @ac3 @smoke @regression |
| AC4 | Find coldest US state via metadata input file              | AC4_ColdestUSState.feature        | @ac4 @smoke @regression |
