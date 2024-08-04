package com.constructiveactivists.usermanagementmodule.controllers.organization.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequest {

    @NotNull
    @Valid
    @Schema(description = "Identificador del usuario al que pertenece la organización")
    private Integer userId;

    @NotNull
    @Valid
    @Schema(description = "Informacion de la organización")
    private InstitutionalInformationRequest institutionalInformation;

    @NotNull
    @Valid
    @Schema(description = "Informacion de contacto de la organización")
    private ContactInformationRequest contactInformation;
}
