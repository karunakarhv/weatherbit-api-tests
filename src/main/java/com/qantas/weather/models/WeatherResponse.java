package com.qantas.weather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("data")
    private List<WeatherData> data;

    // ----- Getters / Setters -----

    public Integer getCount()           { return count; }
    public void setCount(Integer count) { this.count = count; }

    public List<WeatherData> getData()              { return data; }
    public void setData(List<WeatherData> data)     { this.data = data; }

    /**
     * Convenience method: returns the first (and usually only) observation.
     */
    public WeatherData getFirstObservation() {
        if (data == null || data.isEmpty()) {
            throw new IllegalStateException(
                    "No weather data found in response. 'data' array is empty or null.");
        }
        return data.get(0);
    }
}