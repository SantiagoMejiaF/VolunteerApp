package com.constructiveactivists.missionandactivitymodule.entities.activity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "LIDER_COMUNIDAD", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class PersonalDataCommunityLeaderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "NOMBRE", length = 200, nullable = false)
    private String nameCommunityLeader;

    @Column(name = "CORREO", length = 200, nullable = false)
    private String emailCommunityLeader;

    @Column(name = "CELULAR", length = 200, nullable = false)
    private String phoneCommunityLeader;
}
