package com.constructiveactivists.missionandactivitymodule.entities.activity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "RESENA", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "INTEGER", nullable = false)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ACTIVIDAD_ID", referencedColumnName = "ID")
    @Comment("Actividad a la que pertenece esta reseña")
    @JsonIgnore
    private ActivityEntity activity;

    @Column(name = "DESCRIPCION", length = 1000, nullable = false)
    @Comment("Descripción breve de la reseña")
    private String description;

    @ElementCollection
    @CollectionTable(name = "IMAGENES_RESENA", schema = "MODULO_GESTION_MISIONES_Y_ACTIVIDADES", joinColumns = @JoinColumn(name = "RESENA_ID"))
    @Column(name = "IMAGEN_URL", length = 1000)
    @Comment("URL de imágenes asociadas a la reseña")
    private List<String> imageUrls;

    @Comment("Fecha de creación de la reseña")
    @Column(name = "FECHA_CREACION", nullable = false)
    private LocalDate creationDate;

    @Column(name = "LIKES", columnDefinition = "INTEGER", nullable = false)
    @Comment("Número de likes en la reseña")
    private Integer likes;

    @Column(name = "CALIFICACION", columnDefinition = "INTEGER", nullable = false)
    @Comment("Calificación de la reseña")
    private Integer rating;
}
