package com.constructiveactivists.externalservicesmodule.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityModel {

    @JsonProperty("name")
    private String name;
}
