package com.constructiveactivists.organizationmodule.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class CoordinatorAvailabilityModel {

    private Integer organizationId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;
}
