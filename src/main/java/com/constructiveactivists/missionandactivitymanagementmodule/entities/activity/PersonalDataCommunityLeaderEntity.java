package com.constructiveactivists.missionandactivitymanagementmodule.entities.activity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "LIDER_COMUNIDAD_SEQ", sequenceName = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES.LIDER_COMUNIDAD_SEQ",
        allocationSize = 1)
@Table(name = "LIDER_COMUNIDAD", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class PersonalDataCommunityLeaderEntity {

    @Id
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "NOMBRE", length = 200, nullable = false)
    private String nameCommunityLeader;

    @Column(name = "CEDULA", length = 200, nullable = false)
    private String identificationCard;

    @Column(name = "CORREO", length = 200, nullable = false)
    private String emailCommunityLeader;

    @Column(name = "CELULAR", length = 200, nullable = false)
    private String phoneCommunityLeader;
}
