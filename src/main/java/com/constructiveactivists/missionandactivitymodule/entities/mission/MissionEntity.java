package com.constructiveactivists.missionandactivitymodule.entities.mission;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.MissionTypeEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VolunteerMissionRequirementsEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.SkillEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "MISION", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "FECHA_DE_CREACION", columnDefinition = "TIMESTAMP", nullable = false)
    @Comment("Fecha de creación de la misión")
    private LocalDateTime createdAt;

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

    @Column(name = "DEPARTAMENTO", length = 200, nullable = false)
    @Comment("Departamento donde se llevará a cabo la misión")
    private String department;

    @Column(name = "VISIBILIDAD", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("Visibilidad de la misión")
    private VisibilityEnum visibility;

    @Column(name = "ESTADO", length = 12, nullable = false)
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

    @ElementCollection(targetClass = InterestEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "INTERESES_REQUERIDOS", joinColumns = @JoinColumn(name = "MISSION_ID"),
            schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
    @Column(name = "INTERESES")
    private List<InterestEnum> requiredInterestsList;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
