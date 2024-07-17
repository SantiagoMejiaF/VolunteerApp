package com.constructiveactivists.usermanagementmodule.entities.volunteer;

import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.AvailabilityEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.InterestEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.SkillEnum;
import com.constructiveactivists.usermanagementmodule.entities.volunteer.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "VOLUNTEERING_INFO_SEQ", sequenceName = "VOLUNTEERING_INFO_SEQ", allocationSize = 1)
@Table(name = "INFORMACION_VOLUNTARIADO")
public class VolunteeringInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOLUNTEERING_INFO_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "FOTO_PERFIL", length = 50, nullable = false)
    private String profilePicture;

    @Column(name = "FECHA_REGISTRO", columnDefinition = "DATE", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "HORAS_VOLUNTARIADAS", nullable = false)
    private int volunteeredHours;

    @ElementCollection(targetClass = AvailabilityEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "DIAS_DISPONIBLES", joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"))
    @Column(name = "DIA")
    private List<AvailabilityEnum> availabilityDaysList;

    @ElementCollection(targetClass = InterestEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "INTERESES_VOLUNTARIO", joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"))
    @Column(name = "INTERESES")
    private List<InterestEnum> interestsList;

    @ElementCollection(targetClass = SkillEnum.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "HABILIDADES", joinColumns = @JoinColumn(name = "INFORMACION_VOLUNTARIADO_ID"))
    @Column(name = "HABILIDADES")
    private List<SkillEnum> skillsList;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", length = 9)
    private StatusEnum status;
}
