package com.constructiveactivists.missionandactivitymanagementmodule.entities.postulation;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.postulation.enums.PostulationStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "POSTULATION_SEQ", sequenceName = "modulo_gestion_misiones_y_actividades.SEQ_POSTULACION", allocationSize = 1)
@Table(name = "POSTULACION", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class PostulationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POSTULATION_SEQ")
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "VOLUNTARIO_ID", columnDefinition = "INTEGER", nullable = false)
    @Comment("Voluntario que se postula a la misión o actividad")
    private Integer volunteerId;

    @Column(name = "ORGANIZACION_ID", columnDefinition = "INTEGER")
    @Comment("ID de la organización asociada a la postulación, si corresponde")
    private Integer organizationId;

    @ManyToOne
    @JoinColumn(name = "ACTIVIDAD_ID", nullable = false)
    @Comment("Actividad a la que se está postulando el voluntario (puede ser nulo si es una misión)")
    private ActivityEntity activity;

    @Column(name = "ESTADO", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("Estado de la postulación")
    private PostulationStatusEnum status;

    @Column(name = "FECHA_POSTULACION", columnDefinition = "DATE", nullable = false)
    @Comment("Fecha en la que se realizó la postulación")
    private LocalDate applicationDate;

    @Column(name = "FECHA_APROBACION", columnDefinition = "DATE")
    @Comment("Fecha en la que se aprobó la postulación (puede ser nulo si aún no ha sido aprobada)")
    private LocalDate approvalDate;

    @Column(name = "COMENTARIOS", length = 1000)
    @Comment("Comentarios adicionales sobre la postulación")
    private String comments;
}
