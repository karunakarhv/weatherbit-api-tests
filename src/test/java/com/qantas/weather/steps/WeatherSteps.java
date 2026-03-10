package com.qantas.weather.steps;

import com.qantas.weather.actions.WeatherAPI;
import com.qantas.weather.models.StateMetadata;
import com.qantas.weather.models.WeatherData;
import com.qantas.weather.models.WeatherResponse;
import com.qantas.weather.utils.TestDataReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Steps;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// AssertJ for assertions
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class WeatherSteps {

    private static final Logger log = LoggerFactory.getLogger(WeatherSteps.class);

    @Steps
    WeatherAPI weatherApi;

    private Response lastResponse;
    private final List<Response> listOfResponses = new ArrayList<>();
    private Map<String, Double> cityTemperatures = new HashMap<>();
    private Map<String, Double> stateTemperatures = new HashMap<>();
    private List<StateMetadata.StateEntry> usStates;

    @Given("The WeatherBit API is available")
    public void theWeatherBitApiIsAvailable() {
        weatherApi.healthCheck();
    }

    @When("I request current weather for the following cities:")
    public void getWeatherFromFollowingCities(DataTable dataTable){
        List<Map<String, String>> rows = dataTable.asMaps();
        for(Map<String, String> row:rows) {
            String city = row.get("city");
            String countryCode = row.get("country");
            log.info("Getting Current Weather by City: [{}]", city);
            Response resp = weatherApi.getCurrentWeatherByCity(city, countryCode);
            listOfResponses.add(resp);
        }
    }

    @Then("Each City should return a 200 status code")
    public void eachCityShouldReturn200Code(){
        for(Response resp:listOfResponses){
            assertThat(resp.statusCode()).as("Expected 200 but got %d", resp.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        }
    }

    @And("Each City response should contain a valid temperature in Celsius")
    public void theResponseContainsEachCityValidTemperatureInCelsius(){
        for(Response resp:listOfResponses){
            WeatherData weatherData = resp.as(WeatherResponse.class).getFirstObservation();
            assertThat(weatherData.getTemperature())
                    .as("Temperature for %s should be within realistic range", weatherData.getCityName())
                    .isBetween(-90.0, 60.0);
        }
    }

    @And("Each City response should contain a non-empty city name")
    public void theResponseContainsNonEmptyCityName(){
        for(Response resp:listOfResponses){
            WeatherData weatherData = resp.as(WeatherResponse.class).getFirstObservation();
            String cityName = weatherData.getCityName();
            assertThat(cityName)
                    .as("City Names should not be null or empty")
                    .isNotBlank()
                    .isNotNull();
        }
    }

    @And("Each City response should contain weather description")
    public void theResponseContainEachCityWeatherDescription(){
        for(Response resp:listOfResponses){
            WeatherData weatherData = resp.as(WeatherResponse.class).getFirstObservation();
            WeatherData.WeatherDescription weatherDescription = weatherData.getWeather();
            assertThat(weatherDescription.getDescription())
                    .as("Weather Description should not be null or empty")
                    .isNotNull()
                    .isNotBlank();
            assertThat(weatherDescription.getIcon())
                    .as("Weather Description Icon should not be null or empty")
                    .isNotNull()
                    .isNotBlank();
        }
    }

    @Then("the response should contain temperature data")
    public void theResponseShouldContainTemperatureData() {
        WeatherResponse wr = lastResponse.as(WeatherResponse.class);
        assertThat(wr.getFirstObservation().getTemperature())
                .as("Temperature should not be null")
                .isNotNull();
    }

    @Then("the response should contain the city name {string}")
    public void theResponseShouldContainTheCityName(String expectedCity) {
        WeatherResponse wr = lastResponse.as(WeatherResponse.class);
        String actualCity = wr.getFirstObservation().getCityName();
        assertThat(actualCity).as("City name in response").isNotBlank();
        // WeatherBit may return a slightly different casing/spelling,
        // so we do a case-insensitive contains check.
        assertThat(actualCity.toLowerCase())
                .as("Response city name should contain '%s'", expectedCity)
                .contains(expectedCity.toLowerCase().split(" ")[0]);
    }

    // ================================================================
    // ERROR HANDLING — HTTP 400 / 403 / 204 / 500
    // ================================================================

    @When("I request current weather for city {string} in country {string}")
    public void iRequestCurrentWeatherForCity(String city, String country) {
        lastResponse = weatherApi.getCurrentWeatherByCity(city, country);
    }

    @When("I request current weather with no location parameters")
    public void iRequestCurrentWeatherWithNoLocationParameters() {
        lastResponse = weatherApi.getCurrentWeatherWithNoLocationParams();
    }

    @When(
            "I request current weather for coordinates latitude {string} and longitude {string}"
    )
    public void iRequestCurrentWeatherForCoordinates(String lat, String lon) {
        lastResponse =
                weatherApi.getCurrentWeatherByCoordinates(
                        Double.parseDouble(lat),
                        Double.parseDouble(lon)
                );
    }


    @When("I request current weather for city {string} in country {string} without an API key")
    public void iRequestCurrentWeatherWithoutApiKey(String city, String country) {
        lastResponse = weatherApi.getCurrentWeatherWithoutApiKey(city, country);
    }

    @When("I request current weather for city {string} in country {string} with an invalid API key")
    public void iRequestCurrentWeatherWithInvalidApiKey(String city, String country) {
        lastResponse = weatherApi.getCurrentWeatherWithInvalidApiKey(city, country);
    }

    @Then("the response body should contain an error message")
    public void theResponseBodyShouldContainAnErrorMessage() {
        String error = lastResponse.jsonPath().getString("error");
        assertThat(error)
                .as("Response body should contain a non-blank 'error' field")
                .isNotBlank();
        log.info("Error message in response: {}", error);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        assertThat(lastResponse.getStatusCode())
                .as("HTTP status code")
                .isEqualTo(expectedStatusCode);
    }

    @Then("the response body should contain an authentication error message")
    public void theResponseBodyShouldContainAnAuthenticationErrorMessage() {
        String error = lastResponse.jsonPath().getString("error");
        assertThat(error)
                .as("Response body should contain an authentication-related error")
                .isNotBlank();
        assertThat(error.toLowerCase())
                .as("Error message should reference 'API key'")
                .contains("api key");
        log.info("Auth error message in response: {}", error);
    }

    @Then("the response body should have a valid JSON structure")
    public void theResponseBodyShouldHaveValidJsonStructure() {
        WeatherResponse wr = lastResponse.as(WeatherResponse.class);
        assertThat(wr.getData())
                .as("'data' array in response should not be null or empty")
                .isNotNull()
                .isNotEmpty();
        assertThat(wr.getCount())
                .as("'count' field should be >= 1")
                .isGreaterThanOrEqualTo(1);
    }

    @Then("the response latitude should be approximately {string}")
    public void theResponseLatitudeShouldBeApproximately(String expectedLat) {
        WeatherData obs = lastResponse
                .as(WeatherResponse.class)
                .getFirstObservation();
        assertThat(obs.getLatitude())
                .as("Latitude should be approximately %s", expectedLat)
                .isCloseTo(Double.parseDouble(expectedLat), within(1.0));
    }

    @Then("the response longitude should be approximately {string}")
    public void theResponseLongitudeShouldBeApproximately(String expectedLon) {
        WeatherData obs = lastResponse
                .as(WeatherResponse.class)
                .getFirstObservation();
        assertThat(obs.getLongitude())
                .as("Longitude should be approximately %s", expectedLon)
                .isCloseTo(Double.parseDouble(expectedLon), within(1.0));
    }

    @Then("the response should contain a non-empty weather description")
    public void theResponseShouldContainNonEmptyWeatherDescription() {
        WeatherData obs = lastResponse
                .as(WeatherResponse.class)
                .getFirstObservation();
        assertThat(obs.getWeather()).isNotNull();
        assertThat(obs.getWeather().getDescription())
                .as("Weather description should not be blank")
                .isNotBlank();
    }

    @Then("the response should contain wind speed data")
    public void theResponseShouldContainWindSpeedData() {
        WeatherData obs = lastResponse
                .as(WeatherResponse.class)
                .getFirstObservation();
        assertThat(obs.getWindSpeed())
                .as("Wind speed should not be null and >= 0")
                .isNotNull()
                .isGreaterThanOrEqualTo(0.0);
    }

    @Then("the response should contain humidity data")
    public void theResponseShouldContainHumidityData() {
        WeatherData obs = lastResponse
                .as(WeatherResponse.class)
                .getFirstObservation();
        assertThat(obs.getHumidity())
                .as("Humidity should be between 0 and 100")
                .isNotNull()
                .isBetween(0, 100);
    }

    @Then("the response should contain observation datetime")
    public void theResponseShouldContainObservationDatetime() {
        WeatherData obs = lastResponse
                .as(WeatherResponse.class)
                .getFirstObservation();
        assertThat(obs.getObservationDatetime())
                .as("Observation datetime should not be blank")
                .isNotBlank();
    }

    //==============================================
    // AC3 - Warmest Australian Capital City
    //==============================================
    @Given("the list of Australian capital cities:")
    public void theListOfAustralianCapitalCities(DataTable dataTable) {
        cityTemperatures.clear();
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            // Store city → "AU" mapping temporarily; temperatures fetched in When step
            cityTemperatures.put(row.get("city"), null);
        }
        log.info("Loaded {} Australian capital cities from DataTable", rows.size());
    }

    @When("I retrieve current temperature for each Australian capital city")
    public void iRetrieveCurrentTemperatureForEachAustralianCapitalCity() {
        Map<String, String> citiesWithCountry = new HashMap<>();
        for (String city : cityTemperatures.keySet()) {
            citiesWithCountry.put(city, "AU");
        }
        cityTemperatures = weatherApi.collectTemperaturesForCities(citiesWithCountry);
    }

    @Then("I should be able to identify the warmest Australian capital city")
    public void iShouldBeAbleToIdentifyTheWarmestAustralianCapitalCity() {
        assertThat(cityTemperatures).isNotEmpty();
        var maxEntry = cityTemperatures.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (maxEntry.isEmpty()) {
            throw new AssertionError("Could not determine warmest city");
        }

        String warmest = maxEntry.get().getKey();
        double maxTemp = cityTemperatures.get(warmest);
        log.info(
                "RESULT: Warmest Australian capital city is {} at {}°C",
                warmest,
                maxTemp
        );
        // Store result in step context for the And step below
        System.setProperty("test.result.warmest.city", warmest);
        System.setProperty("test.result.warmest.temp", String.valueOf(maxTemp));
    }

    @And("the warmest city result should be logged with its temperature")
    public void theWarmestCityResultShouldBeLoggedWithItsTemperature() {
        String city = System.getProperty("test.result.warmest.city");
        String temp = System.getProperty("test.result.warmest.temp");
        assertThat(city).isNotBlank();
        log.info("Confirmed: Warmest city = {} at {}°C", city, temp);
    }

    //==============================================
    // AC4 - Coldest US State
    //==============================================
    @Given("the list of US states is loaded from the metadata file {string}")
    public void theListOfUsStatesIsLoadedFromMetadataFile(String filePath) {
        StateMetadata metadata = TestDataReader.loadUsStates(filePath);
        usStates = metadata.getStates();
        assertThat(usStates)
                .as("US states metadata file should contain entries")
                .isNotEmpty();
        log.info("Loaded {} US states from {}", usStates.size(), filePath);
    }

    @When("I retrieve current temperature for the capital city of each US state")
    public void iRetrieveCurrentTemperatureForEachUSStateCapital() {
        Map<String, String> stateCapitalMap = new HashMap<>();
        for (StateMetadata.StateEntry entry : usStates) {
            stateCapitalMap.put(entry.getState(), entry.getCapital());
        }
        stateTemperatures =
                weatherApi.collectTemperaturesForStateCapitals(stateCapitalMap);
    }

    @Then("I should be able to identify the coldest US state")
    public void iShouldBeAbleToIdentifyTheColdestUsState() {
        assertThat(stateTemperatures).isNotEmpty();
        String coldest = stateTemperatures
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new AssertionError("Could not determine coldest state")
                );
        double minTemp = stateTemperatures.get(coldest);
        String capital = usStates
                .stream()
                .filter(s -> s.getState().equals(coldest))
                .map(StateMetadata.StateEntry::getCapital)
                .findFirst()
                .orElse("Unknown");
        log.info(
                "RESULT: Coldest US State is {} (capital: {}) at {}°C",
                coldest,
                capital,
                minTemp
        );
        System.setProperty("test.result.coldest.state", coldest);
        System.setProperty("test.result.coldest.capital", capital);
        System.setProperty("test.result.coldest.temp", String.valueOf(minTemp));
    }

    @And(
            "the coldest state result should be logged with its capital city and temperature"
    )
    public void theColdestStateResultShouldBeLogged() {
        String state = System.getProperty("test.result.coldest.state");
        String capital = System.getProperty("test.result.coldest.capital");
        String temp = System.getProperty("test.result.coldest.temp");
        assertThat(state).isNotBlank();
        log.info(
                "Confirmed: Coldest US state = {} (capital: {}) at {}°C",
                state,
                capital,
                temp
        );
    }
}
