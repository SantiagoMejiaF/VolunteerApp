package com.constructiveactivists.volunteermodule.entities.volunteer;

import com.constructiveactivists.volunteermodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.VolunteerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "INFORMACION_VOLUNTARIADO", schema = "MODULO_GESTION_VOLUNTARIOS")
public class VolunteeringInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "HORAS_VOLUNTARIADAS_TOTALES", nullable = false)
    private int volunteeredTotalHours;

    @ElementCollection
    @CollectionTable(
            name = "ACTIVIDADES_COMPLETADAS_VOLUNTARIO",
            joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"),
            schema = "MODULO_GESTION_VOLUNTARIOS"
    )
    @Column(name = "ACTIVIDAD_COMPLETADA", nullable = false)
    private List<Integer> activitiesCompleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_VOLUNTARIO", length = 10, nullable = false)
    private VolunteerType volunteerType;

    @ElementCollection(targetClass = AvailabilityEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "DIAS_DISPONIBLES_VOLUNTARIO", joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"),
            schema = "MODULO_GESTION_VOLUNTARIOS")
    @Column(name = "DIA")
    private List<AvailabilityEnum> availabilityDaysList;

    @ElementCollection(targetClass = InterestEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "INTERESES_VOLUNTARIO", joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"),
            schema = "MODULO_GESTION_VOLUNTARIOS")
    @Column(name = "INTERESES")
    private List<InterestEnum> interestsList;

    @ElementCollection(targetClass = SkillEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "HABILIDADES_VOLUNTARIO", joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"),
            schema = "MODULO_GESTION_VOLUNTARIOS")
    @Column(name = "HABILIDADES")
    private List<SkillEnum> skillsList;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDateTime registrationDate;
}
