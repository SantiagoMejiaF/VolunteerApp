package com.constructiveactivists.volunteermanagementmodule.entities;

import com.constructiveactivists.volunteermanagementmodule.entities.enums.AvailabilityEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.InterestEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.SkillEnum;
import com.constructiveactivists.volunteermanagementmodule.entities.enums.VolunteerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "VOLUNTEERING_INFO_SEQ", sequenceName = "volunteer_management_module.VOLUNTEERING_INFO_SEQ",
        allocationSize = 1)
@Table(name = "INFORMACION_VOLUNTARIADO", schema = "VOLUNTEER_MANAGEMENT_MODULE")
public class VolunteeringInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOLUNTEERING_INFO_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "HORAS_VOLUNTARIADAS", nullable = false)
    private int volunteeredHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_VOLUNTARIO", length = 10, nullable = false)
    private VolunteerType volunteerType;

    @ElementCollection(targetClass = AvailabilityEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "DIAS_DISPONIBLES_VOLUNTARIO", joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"),
            schema = "USER_MANAGEMENT_MODULE")
    @Column(name = "DIA")
    private List<AvailabilityEnum> availabilityDaysList;

    @ElementCollection(targetClass = InterestEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "INTERESES_VOLUNTARIO", joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"),
            schema = "USER_MANAGEMENT_MODULE")
    @Column(name = "INTERESES")
    private List<InterestEnum> interestsList;

    @ElementCollection(targetClass = SkillEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "HABILIDADES_VOLUNTARIO", joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"),
            schema = "USER_MANAGEMENT_MODULE")
    @Column(name = "HABILIDADES")
    private List<SkillEnum> skillsList;
}