package com.constructiveactivists.usermanagementmodule.entities.organization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "ORGANIZACION_SEQ", sequenceName = "SEQ_ORGANIZACION", allocationSize = 1)
@Table(name = "ORGANIZACION", schema = "USERMANAGEMENT")
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORGANIZACION_SEQ")
    @SequenceGenerator(name = "ORGANIZACION_SEQ", sequenceName = "SEQ_ORGANIZACION", allocationSize = 1)
    private Integer id;

    @Column(name = "USER_ID")
    private Integer userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INFORMACION_INSTITUCIONAL_ID", referencedColumnName = "ID")
    private InstitutionalInformationEntity institutionalInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INFORMACION_CONTACTO_ID", referencedColumnName = "ID")
    private ContactInformationEntity contactInformation;

}
