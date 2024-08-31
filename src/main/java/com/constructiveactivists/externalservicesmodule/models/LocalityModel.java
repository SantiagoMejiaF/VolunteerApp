package com.constructiveactivists.externalservicesmodule.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalityModel {
    @JsonProperty("tags")
    private Tags tags;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tags {
        @JsonProperty("name")
        private String name;
    }
}
