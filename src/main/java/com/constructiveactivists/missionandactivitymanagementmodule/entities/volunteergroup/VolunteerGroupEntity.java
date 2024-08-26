package com.constructiveactivists.missionandactivitymanagementmodule.entities.volunteergroup;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "GRUPO_VOLUNTARIO", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class VolunteerGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "ORGANIZACION_ID", columnDefinition = "INTEGER")
    @Comment("Organización que creó el grupo de voluntarios")
    private Integer organizationId;

    @ManyToOne
    @JoinColumn(name = "ACTIVITY_ID", nullable = false)
    @Comment("Actividad a la que está asignado el grupo de voluntarios")
    private ActivityEntity activity;

    @Column(name = "NOMBRE", length = 100, nullable = false)
    @Comment("Nombre del grupo de voluntarios")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "MIEMBROS_GRUPO_VOLUNTARIO",
            schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES",
            joinColumns = @JoinColumn(name = "GRUPO_ID"),
            inverseJoinColumns = @JoinColumn(name = "VOLUNTARIO_ID")
    )
    private List<VolunteerEntity> volunteers;

    @Column(name = "NUMERO_VOLUNTARIOS_REQUERIDOS", columnDefinition = "INTEGER", nullable = false)
    @Comment("Número de voluntarios requeridos para el grupo")
    private Integer numberOfVolunteersRequired;

    @Column(name = "OBSERVACIONES", length = 1000)
    @Comment("Observaciones adicionales sobre el grupo de voluntarios")
    private String observations;
}
