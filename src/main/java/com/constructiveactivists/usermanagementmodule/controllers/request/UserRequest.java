package com.constructiveactivists.usermanagementmodule.controllers.request;

import com.constructiveactivists.usermanagementmodule.entities.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class UserRequest {

    @NotBlank
    @Size(min = 1, max = 20)
    @Schema(description = "Nombre del usuario")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Apellido del usuario")
    private String lastName;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Correo electronico del usuario")
    private String email;

    @NotBlank
    @Size(min = 1, max = 16)
    @Schema(description = "Contraseña del usuario")
    private String password;

    @NotBlank
    @Size(min = 1, max = 3)
    @Schema(description = "Edad del usuario")
    private String age;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de registro del usuario")
    private LocalDate registrationDate;

    @NotBlank
    @Size(min = 1, max = 10)
    @Schema(description = "Numero de celular del usuario")
    private String phoneNumber;

    @NotNull
    @Schema(description = "Rol del usuario")
    private Role role;
}
