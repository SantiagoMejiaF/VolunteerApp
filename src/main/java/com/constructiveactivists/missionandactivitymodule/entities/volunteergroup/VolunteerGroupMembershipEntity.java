package com.constructiveactivists.missionandactivitymodule.entities.volunteergroup;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;

@Entity
@Getter
@Setter
@Table(name = "MIEMBROS_GRUPO_VOLUNTARIO", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class VolunteerGroupMembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "GRUPO_ID")
    private Integer groupId;

    @Column(name = "VOLUNTARIO_ID", nullable = false)
    private Integer volunteerId;
}
