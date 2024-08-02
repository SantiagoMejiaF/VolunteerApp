package com.constructiveactivists.usermanagementmodule.controllers.user.request;

import com.constructiveactivists.usermanagementmodule.entities.user.enums.Role;
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
    @Size(min = 1, max = 20)
    @Schema(description = "Apellido del usuario")
    private String lastName;

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "Correo electronico del usuario")
    private String email;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de registro del usuario")
    private LocalDate registrationDate;

    @NotNull
    @Size(min = 1, max = 2048)
    @Schema(description = "Imagen del usuario")
    private String image;

    @NotNull
    @Schema(description = "Rol del usuario")
    private Role role;
}
