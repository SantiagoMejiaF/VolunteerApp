package com.constructiveactivists.dashboardsandreportsmodule.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerInfoResponse {
    private String name;
    private String image;
    private String email;
    private String identificationCard;
    private Integer hoursDone;
}
