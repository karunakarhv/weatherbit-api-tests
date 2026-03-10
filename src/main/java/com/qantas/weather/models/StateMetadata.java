package com.qantas.weather.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StateMetadata {

    @JsonProperty("states")
    private List<StateEntry> states;

    public List<StateEntry> getStates()                 { return states; }
    public void setStates(List<StateEntry> states)      { this.states = states; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StateEntry {

        @JsonProperty("state")
        private String state;

        @JsonProperty("capital")
        private String capital;

        public String getState()           { return state; }
        public void setState(String state) { this.state = state; }

        public String getCapital()             { return capital; }
        public void setCapital(String capital) { this.capital = capital; }
    }
}