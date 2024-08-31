package com.constructiveactivists.externalservicesmodule.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentModel {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;
}
