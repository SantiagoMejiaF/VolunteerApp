package com.constructiveactivists.missionandactivitymodule.controllers.request.volunteergroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerGroupMembershipRequest {

    @NotNull
    @Schema(description = "Identificador del voluntario", example = "1")
    private Integer volunteerId;
}
