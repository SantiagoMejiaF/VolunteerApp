package com.constructiveactivists.organizationmanagementmodule.controllers.request;

import com.constructiveactivists.organizationmanagementmodule.entities.enums.OrganizationType;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.SectorType;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.VolunteeringType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Schema(description = "Identificador del usuario", example = "1")
    private Integer userId;

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Nombre de la organización", example = "Pontificia Universidad Javeriana")
    private String organizationName;

    @NotNull
    @Schema(description = "Tipo de organización", example = "INSTITUCION_EDUCATIVA")
    private OrganizationType organizationType;

    @NotNull
    @Schema(description = "Sector de la organización", example = "EDUCACION")
    private SectorType sectorType;

    @NotNull
    @Schema(description = "Tipo de voluntariado", example = "ADJUNTA")
    private VolunteeringType volunteeringType;

    @NotBlank
    @Size(max = 10)
    @Schema(description = "Número de identificación tributaria", example = "1006945125")
    private String nit;

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Dirección principal de la organización", example = "Carrera 7 N° 40-62")
    private String address;

    @NotBlank
    @Size(max = 10)
    @Schema(description = "Número de teléfono principal de la organización", example = "3223045923")
    private String phoneNumber;

    @NotBlank
    @Email
    @Size(max = 50)
    @Schema(description = "Correo de contacto de la organización", example = "contacto@organizacion.com")
    private String contactEmail;
}