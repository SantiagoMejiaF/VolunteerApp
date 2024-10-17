package com.constructiveactivists.volunteermodule.entities.volunteer;

import com.constructiveactivists.missionandactivitymodule.entities.mission.enums.VisibilityEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "VOLUNTARIO", schema = "MODULO_GESTION_VOLUNTARIOS")
public class VolunteerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "USUARIO_ID", unique = true, nullable = false)
    private Integer userId;

    @Column(name = "VISIBILIDAD", length = 7, nullable = false)
    private VisibilityEnum visibility;

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
