package com.constructiveactivists.volunteermanagementmodule.entities;

import com.constructiveactivists.volunteermanagementmodule.entities.enums.RelationshipEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "INFORMACION_EMERGENCIA_SEQ", sequenceName = "modulo_gestion_voluntarios.SEQ_INFORMACION_EMERGENCIA", allocationSize = 1)
@Table(name = "INFORMACION_EMERGENCIA", schema = "MODULO_GESTION_VOLUNTARIOS")
public class EmergencyInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String emergencyContactFirstName;

    @Column(name = "APELLIDO", length = 50, nullable = false)
    private String emergencyContactLastName;

    @Column(name = "TELEFONO", length = 10, nullable = false)
    private String emergencyContactPhone;

    @Column(name = "CORREO", length = 50, nullable = false)
    private String emergencyContactEmail;

    @Column(name = "PARENTESCO", length = 7, nullable = false)
    private RelationshipEnum emergencyContactRelationship;
}
