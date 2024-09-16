package com.constructiveactivists.missionandactivitymodule.controllers.request.activity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    @NotNull
    @Comment("Descripción breve de la reseña")
    private String description;

    @Comment("URL de imágenes asociadas a la reseña")
    private List<String> imageUrls;
}
