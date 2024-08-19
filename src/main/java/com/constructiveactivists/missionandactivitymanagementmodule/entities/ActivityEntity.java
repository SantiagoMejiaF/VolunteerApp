package com.constructiveactivists.missionandactivitymanagementmodule.entities;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.enums.ActivityStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "ACTIVITY_SEQ", sequenceName = "mission_and_activity_management_module.SEQ_ACTIVITY", allocationSize = 1)
@Table(name = "ACTIVIDAD", schema = "MISSION_AND_ACTIVITY_MANAGEMENT_MODULE")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITY_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "MISSION_ID", nullable = false)
    @Comment("Misión a la que pertenece esta actividad")
    private MissionEntity mission;

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

    @Column(name = "DIRECCION", length = 200, nullable = false)
    @Comment("Dirección donde se llevará a cabo la actividad")
    private String address;

    @Column(name = "NUMERO_VOLUNTARIOS_REQUERIDOS", columnDefinition = "INTEGER", nullable = false)
    @Comment("Número de voluntarios requeridos para la actividad")
    private Integer numberOfVolunteersRequired;

    @Column(name = "HORAS_REQUERIDAS", columnDefinition = "INTEGER")
    @Comment("Horas de voluntariado requeridas para la actividad")
    private Integer requiredHours;

    @Column(name = "ESTADO", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("Estado actual de la actividad")
    private ActivityStatusEnum activityStatus;

    @Column(name = "OBSERVACIONES", length = 1000)
    @Comment("Observaciones adicionales sobre la actividad")
    private String observations;
}
