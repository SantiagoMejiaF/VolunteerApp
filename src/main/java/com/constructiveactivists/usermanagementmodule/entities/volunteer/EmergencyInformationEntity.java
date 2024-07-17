package com.constructiveactivists.usermanagementmodule.entities.volunteer;

import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.RelationshipEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "INFORMACION_EMERGENCIA")
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

    @Column(name = "PARENTESCO", length = 7, nullable = false)
    private RelationshipEnum emergencyContactRelationship;
}
