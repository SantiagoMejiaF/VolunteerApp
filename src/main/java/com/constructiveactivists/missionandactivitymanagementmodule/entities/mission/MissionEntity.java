package com.constructiveactivists.missionandactivitymanagementmodule.entities.mission;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.mission.enums.VolunteerMissionRequirementsEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.SkillEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "MISSION_SEQ", sequenceName = "modulo_gestion_misiones_y_actividades.SEQ_MISION", allocationSize = 1)
@Table(name = "MISION", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MISSION_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "ORGANIZACION_ID")
    @Comment("ID de la organización que publica la misión")
    private Integer organizationId;

    @Column(name = "TIPO_MISION", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("Tipo de misión")
    private MissionTypeEnum missionType;

    @Column(name = "TITULO", length = 100, nullable = false)
    @Comment("Título de la misión")
    private String title;

    @Column(name = "DESCRIPCION", length = 1000, nullable = false)
    @Comment("Descripción detallada de la misión")
    private String description;

    @Column(name = "FECHA_INICIO", columnDefinition = "DATE", nullable = false)
    @Comment("Fecha de inicio de la misión")
    private LocalDate startDate;

    @Column(name = "FECHA_FIN", columnDefinition = "DATE", nullable = false)
    @Comment("Fecha de finalización de la misión")
    private LocalDate endDate;

    @Column(name = "CIUDAD", length = 200, nullable = false)
    @Comment("Ciudad donde se llevará a cabo la misión")
    private String city;

    @Column(name = "VISIBILIDAD", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("Visibilidad de la misión")
    private VisibilityEnum visibility;

    @Column(name = "ESTADO", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("Estado actual de la misión")
    private MissionStatusEnum missionStatus;

    @ElementCollection(targetClass = VolunteerMissionRequirementsEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "REQUISITOS_REQUERIDOS", joinColumns = @JoinColumn(name = "MISSION_ID"),
            schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
    @Column(name = "REQUISITOS")
    private List<VolunteerMissionRequirementsEnum> volunteerMissionRequirementsEnumList;

    @ElementCollection(targetClass = SkillEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "HABILIDADES_REQUERIDAS", joinColumns = @JoinColumn(name = "MISSION_ID"),
            schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
    @Column(name = "HABILIDADES")
    private List<SkillEnum> requiredSkillsList;
}
