package com.constructiveactivists.missionandactivitymodule.controllers.request.activity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class PersonalDataCommunityLeaderRequest {

    @NotBlank(message = "El nombre del líder de comunidad es obligatorio.")
    @Schema(description = "Nombre completo del líder de comunidad", example = "Juan Pérez")
    private String nameCommunityLeader;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El formato del correo electrónico es inválido.")
    @Schema(description = "Correo electrónico del líder de comunidad", example = "juan.perez@example.com")
    private String emailCommunityLeader;

    @NotBlank(message = "El número de celular es obligatorio.")
    @Pattern(regexp = "^\\d{10}$", message = "El número de celular debe tener exactamente 10 dígitos.")
    @Schema(description = "Número de celular del líder de comunidad", example = "3216549870")
    private String phoneCommunityLeader;
}
