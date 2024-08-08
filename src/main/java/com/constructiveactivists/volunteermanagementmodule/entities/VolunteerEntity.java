package com.constructiveactivists.volunteermanagementmodule.entities;

import com.constructiveactivists.volunteermanagementmodule.entities.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "VOLUNTARIO_SEQ", sequenceName = "SEQ_VOLUNTARIO", allocationSize = 1)
@Table(name = "VOLUNTARIO", schema = "VOLUNTEER_MANAGEMENT_MODULE")
public class VolunteerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOLUNTEER_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "USUARIO_ID", nullable = false)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", length = 9)
    private StatusEnum status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INFORMACION_PERSONAL_ID", referencedColumnName = "ID")
    private PersonalInformationEntity personalInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID", referencedColumnName = "ID")
    private VolunteeringInformationEntity volunteeringInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INFORMACION_EMERGENCIA_ID", referencedColumnName = "ID")
    private EmergencyInformationEntity emergencyInformation;
}
