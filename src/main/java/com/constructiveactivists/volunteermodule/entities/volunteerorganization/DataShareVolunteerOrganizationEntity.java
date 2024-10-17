package com.constructiveactivists.volunteermodule.entities.volunteerorganization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DATOS_COMPARTIDOS_VOLUNTARIO_ORGANIZACION", schema = "MODULO_GESTION_VOLUNTARIOS")
public class DataShareVolunteerOrganizationEntity {

    @Id
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer volunteerOrganizationId;

    @Column(name = "HORAS_HECHAS", nullable = false)
    private Integer hoursDone;

    @Column(name = "HORAS_CERTIFICADAS", nullable = false)
    private Integer hoursCertified;

    @Column(name = "HORAS_MENSUALES", nullable = false)
    private Integer monthlyHours;
}
