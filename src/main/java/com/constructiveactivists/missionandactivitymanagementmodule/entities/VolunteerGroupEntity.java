package com.constructiveactivists.missionandactivitymanagementmodule.entities;

import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "VOLUNTEER_GROUP_SEQ", sequenceName = "mission_and_activity_management_module.SEQ_VOLUNTEER_GROUP", allocationSize = 1)
@Table(name = "GRUPO_VOLUNTARIO", schema = "MISSION_AND_ACTIVITY_MANAGEMENT_MODULE")
public class VolunteerGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOLUNTEER_GROUP_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ORGANIZACION_ID")
    @Comment("Organización que creó el grupo de voluntarios")
    private OrganizationEntity organization;

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
            schema = "MISSION_AND_ACTIVITY_MANAGEMENT_MODULE",
            joinColumns = @JoinColumn(name = "GROUPO_ID"),
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
