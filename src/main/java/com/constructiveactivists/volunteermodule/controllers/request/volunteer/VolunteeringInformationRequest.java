package com.constructiveactivists.volunteermodule.controllers.request.volunteer;

import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
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

    @NotEmpty
    @Schema(description = "Días disponibles para voluntariado", example = "[\"LUNES\", \"MIERCOLES\", \"VIERNES\"]")
    private List<AvailabilityEnum> availabilityDaysList;

    @NotEmpty
    @Schema(description = "Intereses del voluntario", example = "[\"MEDIO_AMBIENTE\", \"EDUCACION\", \"SALUD\"]")
    private List<InterestEnum> interestsList;

    @NotEmpty
    @Schema(description = "Habilidades del voluntario", example = "[\"COMUNICACION\", \"TRABAJO_EN_EQUIPO\", \"LIDERAZGO\"]")
    private List<SkillEnum> skillsList;

}
