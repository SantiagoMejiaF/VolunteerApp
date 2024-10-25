package com.constructiveactivists.missionandactivitymodule.entities.activity;

import com.constructiveactivists.missionandactivitymodule.entities.activity.enums.ActivityStatusEnum;
import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ACTIVIDAD", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "MISION_ID", columnDefinition = "INTEGER", nullable = false)
    @Comment("Misión a la que pertenece esta actividad")
    private Integer missionId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LIDER_COMUNIDAD_ID", referencedColumnName = "ID")
    private PersonalDataCommunityLeaderEntity personalDataCommunityLeaderEntity;

    @Column(name = "COORDINADOR_ACTIVIDAD_ID", columnDefinition = "INTEGER", nullable = false)
    @Comment("Identificador del coordinador de la actividad")
    private Integer activityCoordinator;

    @Column(name = "GRUPO_VOLUNTARIO_ID", columnDefinition = "INTEGER", nullable = false)
    private Integer volunteerGroup;

    @Column(name = "TITULO", length = 100, nullable = false)
    @Comment("Título de la actividad")
    private String title;

    @Column(name = "DESCRIPCION", length = 1000)
    @Comment("Descripción de la actividad")
    private String description;

    @Column(name = "FECHA", columnDefinition = "DATE", nullable = false)
    @Comment("Fecha en la que se llevará a cabo la actividad")
    private LocalDate date;

    @Column(name = "HORA_INICIO", columnDefinition = "TIME", nullable = false)
    @Comment("Hora de inicio de la actividad")
    private LocalTime startTime;

    @Column(name = "HORA_FIN", columnDefinition = "TIME", nullable = false)
    @Comment("Hora de finalización de la actividad")
    private LocalTime endTime;

    @Column(name = "CIUDAD", length = 50, nullable = false)
    @Comment("Ciudad donde se llevará a cabo la actividad")
    private String city;

    @Column(name = "LOCALIDAD", length = 50, nullable = false)
    @Comment("Localidad donde se llevará a cabo la actividad")
    private String locality;

    @Column(name = "DIRECCION", length = 200, nullable = false)
    @Comment("Dirección donde se llevará a cabo la actividad")
    private String address;

    @Column(name = "NUMERO_VOLUNTARIOS_REQUERIDOS", columnDefinition = "INTEGER", nullable = false)
    @Comment("Número de voluntarios requeridos para la actividad")
    private Integer numberOfVolunteersRequired;

    @Column(name = "HORAS_REQUERIDAS", columnDefinition = "INTEGER" , nullable = false)
    @Comment("Horas de voluntariado requeridas para la actividad")
    private Integer requiredHours;

    @Column(name = "ESTADO", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("Estado actual de la actividad")
    private ActivityStatusEnum activityStatus;

    @Column(name="VISIBILIDAD", length = 8 , nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("Visibilidad de la actividad")
    private VisibilityEnum visibility;

    @Column(name = "NUMERO_PERSONAS_BENEFICIADAS", columnDefinition = "INTEGER" , nullable = false)
    @Comment("Número de personas beneficiadas por la actividad")
    private Integer numberOfBeneficiaries;

    @Column(name = "OBSERVACIONES", length = 1000)
    @Comment("Observaciones adicionales sobre la actividad")
    private String observations;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceEntity> attendances;
}
