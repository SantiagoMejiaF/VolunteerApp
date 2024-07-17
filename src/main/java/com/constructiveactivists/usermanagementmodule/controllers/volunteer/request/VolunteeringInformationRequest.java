package com.constructiveactivists.usermanagementmodule.controllers.volunteer.request;

import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.StatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteeringInformationRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    @Schema(description = "URL de la foto de perfil", example = "foto_perfil.jpg")
    private String profilePicture;

    @NotEmpty
    @Schema(description = "Días disponibles para voluntariado", example = "[\"LUNES\", \"MIERCOLES\", \"VIERNES\"]")
    private List<AvailabilityEnum> availabilityDaysList;

    @NotEmpty
    @Schema(description = "Intereses del voluntario", example = "[\"MEDIO_AMBIENTE\", \"EDUCACION\", \"SALUD\"]")
    private List<InterestEnum> interestsList;

    @NotEmpty
    @Schema(description = "Habilidades del voluntario", example = "[\"COMUNICACION\", \"TRABAJO_EN_EQUIPO\", \"LIDERAZGO\"]")
    private List<SkillEnum> skillsList;

    @NotNull
    @Schema(description = "Estado del voluntario", example = "ACTIVO")
    private StatusEnum status;
}
