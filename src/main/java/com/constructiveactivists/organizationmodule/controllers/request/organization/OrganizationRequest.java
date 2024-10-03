package com.constructiveactivists.organizationmodule.controllers.request.organization;

import com.constructiveactivists.organizationmodule.entities.organization.enums.OrganizationTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.SectorTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.VolunteeringTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Size(max = 10)
    @Schema(description = "Número de identificación de la persona responsable", example = "1000286185")
    private String responsiblePersonId;

    @NotBlank
    @Size(max = 10)
    @Size(min = 10)
    @Schema(description = "Número de teléfono de la persona responsable", example = "3223045923")
    private String responsiblePersonPhoneNumber;

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Nombre de la organización", example = "Pontificia Universidad Javeriana")
    private String organizationName;

    @NotNull
    @Schema(description = "Tipo de organización", example = "INSTITUCION_EDUCATIVA")
    private OrganizationTypeEnum organizationTypeEnum;

    @NotNull
    @Schema(description = "Sector de la organización", example = "EDUCACION")
    private SectorTypeEnum sectorTypeEnum;

    @NotNull
    @Schema(description = "Tipo de voluntariado", example = "ADJUNTA")
    private VolunteeringTypeEnum volunteeringTypeEnum;

    @NotBlank
    @Size(max = 10)
    @Schema(description = "Número de identificación tributaria", example = "1006945125")
    private String nit;

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Dirección principal de la organización", example = "Carrera 7 N° 40-62")
    private String address;

    @NotNull
    @Schema(description = "Horas requeridas para la certificación", example = "100")
    private Integer requiredCertificationHours;

    @NotNull
    @Schema(description = "Descripción de la organización", example = "Organización sin ánimo de lucro")
    private String description;
}