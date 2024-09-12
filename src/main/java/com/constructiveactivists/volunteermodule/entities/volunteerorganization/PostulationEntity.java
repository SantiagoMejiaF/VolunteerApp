package com.constructiveactivists.volunteermodule.entities.volunteerorganization;

import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "POSTULACION", schema = "MODULO_GESTION_VOLUNTARIOS")
public class PostulationEntity {

    @Id
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer volunteerOrganizationId;

    @Column(name = "ESTADO", length = 10, nullable = false)
    private OrganizationStatusEnum status;

    @Column(name = "FECHA_INSCRIPCION", nullable = false)
    private LocalDate registrationDate;
}
