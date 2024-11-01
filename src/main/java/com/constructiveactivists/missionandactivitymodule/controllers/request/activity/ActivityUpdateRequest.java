package com.constructiveactivists.missionandactivitymodule.controllers.request.activity;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityUpdateRequest {

    private String title;

    private String description;

    private Integer activityCoordinator;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String city;

    private String locality;

    private String address;

    private Integer numberOfVolunteersRequired;

    private Integer requiredHours;

    private Integer numberOfBeneficiaries;

    private String observations;

    private VisibilityEnum visibility;
}
