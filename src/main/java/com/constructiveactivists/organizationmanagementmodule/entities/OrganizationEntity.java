package com.constructiveactivists.organizationmanagementmodule.entities;

import com.constructiveactivists.organizationmanagementmodule.entities.enums.OrganizationType;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.SectorType;
import com.constructiveactivists.organizationmanagementmodule.entities.enums.VolunteeringType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "ORGANIZACION_SEQ", sequenceName = "SEQ_ORGANIZACION", allocationSize = 1)
@Table(name = "ORGANIZACION", schema = "ORGANIZATION_MANAGEMENT_MODULE")
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORGANIZACION_SEQ")
    @SequenceGenerator(name = "ORGANIZACION_SEQ", sequenceName = "SEQ_ORGANIZACION", allocationSize = 1)
    private Integer id;

    @Column(name = "USUARIO_ID", nullable = false)
    private Integer userId;

    @Column(name = "NOMBRE_ORGANIZACION", length = 50, nullable = false)
    private String organizationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ORGANIZACION", length = 21)
    private OrganizationType organizationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "SECTOR", length = 50, nullable = false)
    private SectorType sectorType;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_VOLUNTARIADO", length = 21)
    private VolunteeringType volunteeringType;

    @Column(name = "NIT", length = 10, unique = true, nullable = false)
    private String nit;

    @Column(name = "DIRECCION_PRINCIPAL", length = 50, nullable = false)
    private String address;

    @Column(name = "TELEFONO_DE_CONTACTO", length = 10, nullable = false)
    private String phoneNumber;

    @Column(name = "CORREO_DE_CONTACTO", length = 50, nullable = false)
    private String contactEmail;
}
