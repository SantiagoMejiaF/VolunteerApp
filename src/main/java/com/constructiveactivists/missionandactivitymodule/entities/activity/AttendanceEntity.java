package com.constructiveactivists.missionandactivitymodule.entities.activity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "ASISTENCIA", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class AttendanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ACTIVIDAD_ID", nullable = false)
    private ActivityEntity activity;

    @Column(name = "VOLUNTARIO_ID", columnDefinition = "INTEGER", nullable = false)
    @Comment("ID del voluntario que asistió a la actividad")
    private Integer volunteerId;

    @Column(name = "HORA_INICIO_ASISTENCIA", columnDefinition = "TIME")
    @Comment("Hora en que el voluntario registró su asistencia de inicio")
    private LocalTime checkInTime;

    @Column(name = "HORA_FIN_ASISTENCIA", columnDefinition = "TIME")
    @Comment("Hora en que el voluntario registró su asistencia de fin")
    private LocalTime checkOutTime;
}
