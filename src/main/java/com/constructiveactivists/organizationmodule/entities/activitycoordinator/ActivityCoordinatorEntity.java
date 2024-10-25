package com.constructiveactivists.organizationmodule.entities.activitycoordinator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "COORDINADOR_ACTIVIDAD", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class ActivityCoordinatorEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
        private Integer id;

        @Column(name = "USUARIO_ID", columnDefinition = "INTEGER", nullable = false)
        private Integer userId;

        @Column(name = "ORGANIZACION_ID", columnDefinition = "INTEGER")
        private Integer organizationId;

        @Column(name = "CEDULA", length = 200, nullable = false)
        private String identificationCard;

        @Column(name = "CELULAR", length = 200, nullable = false)
        private String phoneActivityCoordinator;

        @ElementCollection
        @CollectionTable(
                name = "ACTIVIDADES_COMPLETADAS_COORDINADOR",
                schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES",
                joinColumns = @JoinColumn(name = "COORDINADOR_ACTIVIDAD_ID")
        )
        @Column(name = "ACTIVIDAD_ID", columnDefinition = "INTEGER")
        private List<Integer> completedActivities;
}
