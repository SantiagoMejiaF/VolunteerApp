package com.constructiveactivists.volunteermodule.entities.volunteerorganization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "VOLUNTARIO_ORGANIZACION", schema = "MODULO_GESTION_VOLUNTARIOS")
public class VolunteerOrganizationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "VOLUNTARIO_ID", nullable = false)
    private Integer volunteerId;

    @Column(name = "ORGANIZACION_ID", nullable = false)
    private Integer organizationId;
}
