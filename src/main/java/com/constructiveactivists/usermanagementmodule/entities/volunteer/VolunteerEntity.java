package com.constructiveactivists.usermanagementmodule.entities.volunteer;

import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "VOLUNTARIO_SEQ", sequenceName = "SEQ_VOLUNTARIO", allocationSize = 1)
@Table(name = "VOLUNTARIO")
public class VolunteerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOLUNTEER_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INFORMACION_PERSONAL_ID", referencedColumnName = "ID")
    private PersonalInformationEntity personalInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID", referencedColumnName = "ID")
    private VolunteeringInformationEntity volunteeringInformation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "INFORMACION_EMERGENCIA_ID", referencedColumnName = "ID")
    private EmergencyInformationEntity emergencyInformation;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", length = 9)
    private StatusEnum status;
}
