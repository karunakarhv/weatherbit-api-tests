package com.qantas.weather.config;

public class ConfigManager {
    // Weatherbit REST base URL — configurable for proxies / mock servers
    private static final String DEFAULT_BASE_URL = "https://api.weatherbit.io/v2.0";

    private ConfigManager(){

    }

    /**
     * Returns the Weatherbit base URL.
     * Can be overridden with -Dweatherbit.base.url=... for stub/mock testing.
     */
    public static String getBaseUrl() {
        String url = System.getProperty("weatherbit.base.url");
        return (url != null && !url.isBlank()) ? url : DEFAULT_BASE_URL;
    }

    public static String getApiKey(){
        // JVM Property
        String apiKey = System.getProperty("weatherbit.apikey");
        if(apiKey != null && !apiKey.isBlank()) {
            return apiKey;
        }
        // Fallback Env
        apiKey = System.getenv("WEATHERBIT_API_KEY");
        if(apiKey != null && !apiKey.isBlank()) {
            return apiKey;
        }
        else {
            throw new RuntimeException("Weather BIT API Key is not configured." +
                    "Give it via: mvn verify -Dweatherbit.apikey=YOUR_KEY"+
                    "or set environment variable WEATHERBIT_API_KEY");
        }
    }
}
