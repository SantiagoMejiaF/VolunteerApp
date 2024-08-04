package com.constructiveactivists.usermanagementmodule.entities.organization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "INFORMACION_CONTACTO_SEQ", sequenceName = "SEQ_INFORMACION_CONTACTO", allocationSize = 1)
@Table(name = "INFORMACION_CONTACTO", schema = "USERMANAGEMENT")
public class ContactInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INFORMACION_CONTACTO_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "EMAIL", length = 50, nullable = false)
    private String email;

    @Column(name = "TELEFONO", length = 10, nullable = false)
    private String phoneNumber;

    @Column(name = "DIRECCION", length = 50, nullable = false)
    private String address;
}
