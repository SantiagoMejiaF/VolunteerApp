package com.constructiveactivists.organizationmanagementmodule.entities;

import com.constructiveactivists.organizationmanagementmodule.entities.enums.OrganizationTypeEnum;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.SectorTypeEnum;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.VolunteeringTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "ORGANIZACION_SEQ", sequenceName = "modulo_gestion_organizaciones.SEQ_ORGANIZACION", allocationSize = 1)
@Table(name = "ORGANIZACION", schema = "MODULO_GESTION_ORGANIZACIONES")

public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORGANIZACION_SEQ")
    private Integer id;

    @Column(name = "USUARIO_ID", unique = true, nullable = false)
    private Integer userId;

    @Column(name = "CEDULA_RESPONSABLE", length = 10, unique = true, nullable = false)
    private String responsiblePersonId;

    @Column(name = "TELEFONO_RESPONSABLE", length = 10, nullable = false)
    private String responsiblePersonPhoneNumber;

    @Column(name = "NOMBRE_ORGANIZACION", length = 50, nullable = false)
    private String organizationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ORGANIZACION", length = 21)
    private OrganizationTypeEnum organizationTypeEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "SECTOR", length = 50, nullable = false)
    private SectorTypeEnum sectorTypeEnum;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_VOLUNTARIADO", length = 21)
    private VolunteeringTypeEnum volunteeringTypeEnum;

    @Column(name = "NIT", length = 10, unique = true, nullable = false)
    private String nit;

    @Column(name = "DIRECCION_ORGANIZACION", length = 50, nullable = false)
    private String address;
}
