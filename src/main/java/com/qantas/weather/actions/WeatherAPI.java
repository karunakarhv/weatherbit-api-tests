package com.qantas.weather.actions;

import com.qantas.weather.config.ConfigManager;
import com.qantas.weather.models.WeatherResponse;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WeatherAPI {

    private static final Logger log = LoggerFactory.getLogger(WeatherAPI.class);
    private static final String CURRENT_END_POINT = "/current";

    @Step("Verify WeatherBIT API is Available")
    public void healthCheck(){
        Response resp = SerenityRest
                .given()
                .baseUri(ConfigManager.getBaseUrl())
                .queryParam("city", "Sydney")
                .queryParam("country", "AU")
                .queryParam("key", ConfigManager.getApiKey())
                .queryParam("units", "M")
                .when()
                .get(CURRENT_END_POINT);

        log.info("API Health Check returned HTTP {}", resp.getStatusCode());

        if(resp.getStatusCode() >= 500){
            throw new RuntimeException("WeatherBit API is unavailable. HTTP " + resp.getStatusCode());
        }
    }

    @Step("Request current weather for {0}, {1}")
    public Response getCurrentWeatherByCity(String city, String countryCode) {
        Response resp =
                SerenityRest
                        .given()
                        .baseUri(ConfigManager.getBaseUrl())
                        .queryParam("city", city)
                        .queryParam("country", countryCode)
                        .queryParam("key", ConfigManager.getApiKey())
                        .queryParam("units", "M")
                        .when()
                        .get(CURRENT_END_POINT);

        log.info(
                "GET /current?city={}&country={} → HTTP {}",
                city,
                countryCode,
                resp.getStatusCode()
        );
        return resp;
    }
    @Step("Request current weather with no location parameters")
    public Response getCurrentWeatherWithNoLocationParams() {
        Response resp = SerenityRest
                .given()
                .baseUri(ConfigManager.getBaseUrl())
                .queryParam("key", ConfigManager.getApiKey())
                .queryParam("units", "M")
                .when()
                .get(CURRENT_END_POINT);
        log.info("GET /current (no location) → HTTP {}", resp.getStatusCode());
        return resp;
    }

    @Step("Request current weather for {0}, {1} without an API key")
    public Response getCurrentWeatherWithoutApiKey(String city, String countryCode) {
        Response resp = SerenityRest
                .given()
                .baseUri(ConfigManager.getBaseUrl())
                .queryParam("city", city)
                .queryParam("country", countryCode)
                .queryParam("units", "M")
                .when()
                .get(CURRENT_END_POINT);
        log.info("GET /current (no key) city={} → HTTP {}", city, resp.getStatusCode());
        return resp;
    }

    @Step("Request current weather for coordinates lat={0}, lon={1}")
    public Response getCurrentWeatherByCoordinates(
            double latitude,
            double longitude
    ) {
        Response resp = SerenityRest
                        .given()
                        .baseUri(ConfigManager.getBaseUrl())
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .queryParam("key", ConfigManager.getApiKey())
                        .queryParam("units", "M")
                        .when()
                        .get(CURRENT_END_POINT);

        log.info(
                "GET /current?lat={}&lon={} → HTTP {}",
                latitude,
                longitude,
                resp.getStatusCode()
        );
        return resp;
    }

    @Step("Request current weather for {0}, {1} with an invalid API key")
    public Response getCurrentWeatherWithInvalidApiKey(String city, String countryCode) {
        Response resp = SerenityRest
                .given()
                .baseUri(ConfigManager.getBaseUrl())
                .queryParam("city", city)
                .queryParam("country", countryCode)
                .queryParam("key", "INVALIDKEY00000000000000000000000")
                .queryParam("units", "M")
                .when()
                .get(CURRENT_END_POINT);
        log.info("GET /current (invalid key) city={} → HTTP {}", city, resp.getStatusCode());
        return resp;
    }

    @Step("Retrieve temperatures for all cities")
    public Map<String, Double> collectTemperaturesForCities(
            Map<String, String> cities
    ) {
        Map<String, Double> temps = new HashMap<>();
        for (Map.Entry<String, String> entry : cities.entrySet()) {
            Response r = getCurrentWeatherByCity(entry.getKey(), entry.getValue());
            if (r.getStatusCode() == 200) {
                WeatherResponse wr = r.as(WeatherResponse.class);
                double temp = wr.getFirstObservation().getTemperature();
                temps.put(entry.getKey(), temp);
                log.info(
                        "  {} ({}) → {}°C",
                        entry.getKey(),
                        entry.getValue(),
                        temp
                );
            } else {
                log.warn(
                        "  {} returned HTTP {} — skipping",
                        entry.getKey(),
                        r.getStatusCode()
                );
            }
        }
        return temps;
    }

    @Step("Retrieve temperatures for all US state capitals")
    public Map<String, Double> collectTemperaturesForStateCapitals(
            Map<String, String> stateCapitals
    ) {
        Map<String, Double> temps = new HashMap<>();
        for (Map.Entry<String, String> entry : stateCapitals.entrySet()) {
            String stateName = entry.getKey();
            String capitalCity = entry.getValue();

            Response r = getCurrentWeatherByCity(capitalCity, "US");
            if (r.getStatusCode() == 200) {
                WeatherResponse wr = r.as(WeatherResponse.class);
                double temp = wr.getFirstObservation().getTemperature();
                temps.put(stateName, temp);
                log.info("  {} (capital: {}) → {}°C", stateName, capitalCity, temp);
            } else {
                log.warn(
                        "  {} capital {} returned HTTP {} — skipping",
                        stateName,
                        capitalCity,
                        r.getStatusCode()
                );
            }
        }
        return temps;
    }

}
