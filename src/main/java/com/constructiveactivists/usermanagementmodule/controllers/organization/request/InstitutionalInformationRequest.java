package com.constructiveactivists.usermanagementmodule.controllers.organization.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionalInformationRequest {

    @NotBlank
    @Schema(description = "NIT de la organización")
    private String nit;

    @NotBlank
    @Schema(description = "Nombre de la organización")
    private String foundationName;

    @NotBlank
    @Schema(description = "Página web de la organización")
    private String website;

}
