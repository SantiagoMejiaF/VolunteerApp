package com.constructiveactivists.missionandactivitymanagementmodule.entities.activitycoordinator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "COORDINADOR_ACTIVIDAD_SEQ", sequenceName = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES.COORDINADOR_ACTIVIDAD_SEQ",
        allocationSize = 1)
@Table(name = "COORDINADOR_ACTIVIDAD", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class ActivityCoordinatorEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COORDINADOR_ACTIVIDAD_SEQ")
        @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
        private Integer id;

        @Column(name = "USUARIO_ID", columnDefinition = "INTEGER", nullable = false)
        private Long userId;

        @Column(name = "ORGANIZACION_ID", columnDefinition = "INTEGER")
        private Long organizationId;

        @Column(name = "NOMBRE", length = 200, nullable = false)
        private String nameActivityCoordinator;

        @Column(name = "APELLIDO", length = 200, nullable = false)
        private String lastNameActivityCoordinator;

        @Column(name = "CEDULA", length = 200, nullable = false)
        private String identificationCard;

        @Column(name = "CORREO", length = 200, nullable = false)
        private String emailActivityCoordinator;

        @Column(name = "CELULAR", length = 200, nullable = false)
        private String phoneActivityCoordinator;
}
