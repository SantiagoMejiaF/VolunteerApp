package com.constructiveactivists.organizationmodule.controllers.request.activitycoordinator;

import jakarta.validation.constraints.Email;
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

    @NotBlank(message = "El nombre del coordinador de actividad es obligatorio.")
    @Schema(description = "Nombre del coordinador de actividad", example = "María")
    private String nameActivityCoordinator;

    @NotBlank(message = "El apellido del coordinador de actividad es obligatorio.")
    @Schema(description = "Apellido del coordinador de actividad", example = "Gómez")
    private String lastNameActivityCoordinator;

    @NotBlank(message = "La cédula es obligatoria.")
    @Pattern(regexp = "^\\d{10}$", message = "La cédula debe tener exactamente 10 dígitos.")
    @Schema(description = "Cédula del coordinador de actividad", example = "0987654321")
    private String identificationCard;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico es inválido.")
    @Schema(description = "Correo electrónico del coordinador de actividad", example = "maria.gomez@example.com")
    private String emailActivityCoordinator;

    @NotBlank(message = "El número de celular es obligatorio.")
    @Pattern(regexp = "^\\d{10}$", message = "El número de celular debe tener exactamente 10 dígitos.")
    @Schema(description = "Número de celular del coordinador de actividad", example = "3109876543")
    private String phoneActivityCoordinator;

    @Schema(description = "Imagen del coordinador de actividad", example = "https://example.com/image.jpg")
    private String image;
}

