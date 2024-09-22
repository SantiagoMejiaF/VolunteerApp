package com.constructiveactivists.missionandactivitymodule.controllers.request.activity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    @NotNull
    @Comment("Descripción breve de la reseña")
    private String description;

    @Max(5)
    @Min(1)
    @Comment("Calificación de la reseña")
    private Integer rating;

}
