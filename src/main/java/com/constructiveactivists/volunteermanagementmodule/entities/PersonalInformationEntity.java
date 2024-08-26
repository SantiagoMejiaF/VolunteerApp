package com.constructiveactivists.volunteermanagementmodule.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "INFORMACION_PERSONAL_SEQ", sequenceName = "modulo_gestion_voluntarios.SEQ_INFORMACION_PERSONAL", allocationSize = 1)
@Table(name = "INFORMACION_PERSONAL", schema = "MODULO_GESTION_VOLUNTARIOS")
public class PersonalInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "CEDULA", length = 10, unique = true, nullable = false)
    private String identificationCard;

    @Column(name = "DIRECCION", length = 50, nullable = false)
    private String address;

    @Column(name = "AGE", nullable = false)
    private Integer age;

    @Column(name="BORN_DATE", columnDefinition = "DATE", nullable = false)
    private LocalDate bornDate;

    @Column(name = "TELEFONO", length = 10, nullable = false)
    private String phoneNumber;

    @Column(name = "DESCRIPCIÓN_PERSONAL", length = 1000, nullable = false)
    private String personalDescription;
}
