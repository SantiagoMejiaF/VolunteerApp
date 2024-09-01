package com.constructiveactivists.missionandactivitymodule.entities.volunteergroup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
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

    @Column(name = "ACTIVIDAD_ID", columnDefinition = "INTEGER")
    @Comment("Actividad a la que está asignado el grupo de voluntarios")
    private Integer activity;

    @Column(name = "NOMBRE", length = 100, nullable = false)
    @Comment("Nombre del grupo de voluntarios")
    private String name;

    @Column(name = "CANTIDAD_VOLUNTARIOS_REQUERIDOS", columnDefinition = "INTEGER", nullable = false)
    @Comment("Número de voluntarios requeridos para el grupo")
    private Integer numberOfVolunteersRequired;

    @Column(name= "CANTIDAD_DE_VOLUNTARIOS_ACTUAL", columnDefinition = "INTEGER", nullable = false)
    @Comment("Cantidad de voluntarios actualmente en el grupo")
    private Integer currentVolunteers;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "GRUPO_ID")
    private List<VolunteerGroupMembershipEntity> memberships = new ArrayList<>();
}
