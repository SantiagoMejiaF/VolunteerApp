package com.constructiveactivists.usermanagementmodule.entities.volunteer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name =  "INFORMACION_PERSONAL_SEQ", sequenceName = "SEQ_INFORMACION_PERSONAL", allocationSize = 1)
@Table(name = "INFORMACION_PERSONAL")
public class PersonalInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String firstName;

    @Column(name = "APELLIDO", length = 50, nullable = false)
    private String lastName;

    @Column(name = "CEDULA", length = 10, unique = true, nullable = false)
    private String identificationCard;

    @Column(name = "CORREO", length = 50, unique = true, nullable = false)
    private String email;

    @Column(name = "CELULAR", length = 10, nullable = false)
    private String phoneNumber;

    @Column(name = "DIRECCION", length = 50, nullable = false)
    private String address;
}