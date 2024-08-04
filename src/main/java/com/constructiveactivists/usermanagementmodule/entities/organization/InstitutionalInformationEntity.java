package com.constructiveactivists.usermanagementmodule.entities.organization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "INFORMACION_INSTITUCIONAL_SEQ", sequenceName = "SEQ_INFORMACION_INSTITUCIONAL", allocationSize = 1)
@Table(name = "INFORMACION_INSTITUCIONAL" , schema = "USERMANAGEMENT")
public class InstitutionalInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INFORMACION_INSTITUCIONAL_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "NIT", length = 10, unique = true, nullable = false)
    private String nit;

    @Column(name = "NOMBRE_FUNDACION", length = 50, nullable = false)
    private String foundationName;

    @Column(name = "PAGINA_WEB", length = 50, nullable = false)
    private String website;

}
