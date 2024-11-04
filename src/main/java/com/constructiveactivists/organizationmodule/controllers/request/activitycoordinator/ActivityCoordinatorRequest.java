package com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityCoordinatorRequest {

    @NotNull(message = "El ID de la organización es obligatorio.")
    @Schema(description = "ID de la organización asociada al coordinador de actividad", example = "456")
    private Integer organizationId;

    @NotNull(message = "El UserId del coordinador de actividad es obligatorio.")
    @Schema(description = "UserId del coordinador de actividad", example = "123")
    private Integer userId;

    @NotBlank(message = "La cédula es obligatoria.")
    @Pattern(regexp = "^\\d{10}$", message = "La cédula debe tener exactamente 10 dígitos.")
    @Schema(description = "Cédula del coordinador de actividad", example = "0987654321")
    private String identificationCard;

    @NotBlank(message = "El número de celular es obligatorio.")
    @Pattern(regexp = "^\\d{10}$", message = "El número de celular debe tener exactamente 10 dígitos.")
    @Schema(description = "Número de celular del coordinador de actividad", example = "3109876543")
    private String phoneActivityCoordinator;
}
