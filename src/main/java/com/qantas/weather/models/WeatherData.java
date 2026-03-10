package com.qantas.weather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Maps a single weather observation object returned inside
 * the Weatherbit "data" array.
 *
 * Example JSON slice:
 * {
 *   "temp": 22.5,
 *   "city_name": "Sydney",
 *   "country_code": "AU",
 *   "lat": -33.86,
 *   "lon": 151.21,
 *   "weather": { "description": "Clear sky" },
 *   "wind_spd": 4.1,
 *   "rh": 65,
 *   "datetime": "2024-01-15:12"
 * }
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherData {

    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("city_name")
    private String cityName;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("lon")
    private Double longitude;

    @JsonProperty("weather")
    private WeatherDescription weather;

    @JsonProperty("wind_spd")
    private Double windSpeed;

    @JsonProperty("rh")
    private Integer humidity;

    @JsonProperty("datetime")
    private String observationDatetime;

    // ----- Getters -----

    public Double getTemperature()        { return temperature; }
    public String getCityName()           { return cityName; }
    public String getCountryCode()        { return countryCode; }
    public Double getLatitude()           { return latitude; }
    public Double getLongitude()          { return longitude; }
    public WeatherDescription getWeather(){ return weather; }
    public Double getWindSpeed()          { return windSpeed; }
    public Integer getHumidity()          { return humidity; }
    public String getObservationDatetime(){ return observationDatetime; }

    // ----- Setters (required for Jackson deserialization) -----

    public void setTemperature(Double temperature)               { this.temperature = temperature; }
    public void setCityName(String cityName)                     { this.cityName = cityName; }
    public void setCountryCode(String countryCode)               { this.countryCode = countryCode; }
    public void setLatitude(Double latitude)                     { this.latitude = latitude; }
    public void setLongitude(Double longitude)                   { this.longitude = longitude; }
    public void setWeather(WeatherDescription weather)           { this.weather = weather; }
    public void setWindSpeed(Double windSpeed)                   { this.windSpeed = windSpeed; }
    public void setHumidity(Integer humidity)                    { this.humidity = humidity; }
    public void setObservationDatetime(String observationDatetime){ this.observationDatetime = observationDatetime; }

    // ----------------------------------------------------------------
    // Inner class — Weatherbit nests description inside a "weather" obj
    // ----------------------------------------------------------------

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherDescription {

        @JsonProperty("description")
        private String description;

        @JsonProperty("icon")
        private String icon;

        public String getDescription() { return description; }
        public String getIcon()        { return icon; }

        public void setDescription(String description) { this.description = description; }
        public void setIcon(String icon)               { this.icon = icon; }
    }
}
