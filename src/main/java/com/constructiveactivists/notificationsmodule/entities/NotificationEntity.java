package com.constructiveactivists.notificationsmodule.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "NOTIFICACION", schema = "MODULO_GESTION_USUARIOS")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @Column(name = "TITULO", length = 100, nullable = false)
    private String title;

    @Column(name = "DESCRIPCION", length = 255)
    private String description;

    @Column(name = "FECHA_CREACION", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "USUARIO_ID", nullable = false)
    private Integer userId;
}
