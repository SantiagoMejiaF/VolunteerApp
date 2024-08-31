package com.constructiveactivists.volunteermanagementmodule.entities;

import com.constructiveactivists.volunteermanagementmodule.entities.enums.OrganizationStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    @Column(name = "HORAS_HECHAS", nullable = false)
    private Integer hoursDone;

    @Column(name = "HORAS_CERTIFICADAS", nullable = false)
    private Integer hoursCertified;

    @Column(name = "HORAS_MENSUALES", nullable = false)
    private Integer monthlyHours;

    @Column(name = "ESTADO", length = 10, nullable = false)
    private OrganizationStatusEnum status;

    @Column(name = "FECHA_INSCRIPCION", nullable = false)
    private LocalDate registrationDate;
}
