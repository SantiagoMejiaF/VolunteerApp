package com.constructiveactivists.missionandactivitymodule.controllers;

import com.constructiveactivists.missionandactivitymodule.controllers.configuration.activity.ReviewAPI;
import com.constructiveactivists.missionandactivitymodule.controllers.request.activity.ReviewRequest;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ReviewEntity;
import com.constructiveactivists.missionandactivitymodule.mappers.activity.ReviewMapper;
import com.constructiveactivists.missionandactivitymodule.services.activity.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("${request-mapping.controller.review}")
public class ReviewController implements ReviewAPI {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @Override
    public ResponseEntity<String> createReviewForActivity(
            @RequestParam("activityId") Integer activityId,
            @ModelAttribute ReviewRequest reviewRequest){

        try {
            ReviewEntity createdReview = reviewService.createReviewForActivity(activityId, reviewMapper.toDomain(reviewRequest));

            // Construir el HTML para el éxito
            String htmlResponse = buildSuccessHtml(createdReview);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.TEXT_HTML)
                    .body(htmlResponse);

        } catch (EntityNotFoundException e) {
            // Construir el HTML para el caso en que la actividad no sea encontrada
            String errorHtml = buildErrorHtml("Actividad no encontrada", "La actividad con ID " + activityId + " no fue encontrada.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);

        } catch (IllegalStateException e) {
            // Construir el HTML para el caso de error de reseña duplicada
            String errorHtml = buildErrorHtml("Reseña ya existente", "Esta actividad ya tiene una reseña.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorHtml);
        }
    }

    private String buildSuccessHtml(ReviewEntity review) {
        String activityName = review.getActivity().getTitle();
        String reviewDescription = review.getDescription();

        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "   <meta charset='UTF-8'>" +
                "   <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "   <title>Reseña Creada</title>" +
                "   <style>" +
                "       body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }" +
                "       .container { max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); border-radius: 8px; }" +
                "       h1 { color: #1E1450; text-align: center; }" +
                "       p { font-size: 16px; line-height: 1.6; color: #333; }" +
                "       .logo { text-align: center; margin-bottom: 20px; }" +
                "       .logo img { max-width: 150px; height: auto; }" +
                "       .highlight { background-color: #e7f3fe; border-left: 4px solid #2196F3; padding: 10px; margin: 20px 0; }" +
                "       @media (max-width: 600px) {" +
                "           .container { padding: 15px; }" +
                "           h1 { font-size: 24px; }" +
                "           p { font-size: 14px; }" +
                "       }" +
                "   </style>" +
                "</head>" +
                "<body>" +
                "   <div class='container'>" +
                "       <div class='logo'>" +
                "           <img src='https://tinypic.host/images/2024/09/21/logo.png' alt='Logo'>" +
                "       </div>" +
                "       <h1>¡Reseña Creada con Éxito!</h1>" +
                "       <p>Gracias por dejar tu reseña para la actividad <strong>" + activityName + "</strong>.</p>" +
                "       <div class='highlight'>" +
                "           <p><strong>Descripción de la reseña:</strong></p>" +
                "           <p>" + reviewDescription + "</p>" +
                "       </div>" +
                "       <p>Fecha de creación: " + review.getCreationDate() + "</p>" +
                "       <p>¡Esperamos verte pronto en futuras actividades!</p>" +
                "       <p>Si tienes más comentarios o preguntas, no dudes en contactarnos.</p>" +
                "   </div>" +
                "</body>" +
                "</html>";
    }

    private String buildErrorHtml(String errorTitle, String errorMessage) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "   <meta charset='UTF-8'>" +
                "   <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "   <title>Error</title>" +
                "   <style>" +
                "       body { font-family: Arial, sans-serif; background-color: #f8d7da; margin: 0; padding: 0; }" +
                "       .container { max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); border-radius: 8px; border: 1px solid #f5c2c7; }" +
                "       h1 { color: #721c24; text-align: center; }" +
                "       p { font-size: 16px; line-height: 1.6; color: #721c24; }" +
                "       .logo { text-align: center; margin-bottom: 20px; }" +
                "       .logo img { max-width: 150px; height: auto; }" +
                "       .suggestion { background-color: #fff3cd; border-left: 4px solid #ffeeba; padding: 10px; margin: 20px 0; }" +
                "       @media (max-width: 600px) {" +
                "           .container { padding: 15px; }" +
                "           h1 { font-size: 24px; }" +
                "           p { font-size: 14px; }" +
                "       }" +
                "   </style>" +
                "</head>" +
                "<body>" +
                "   <div class='container'>" +
                "       <div class='logo'>" +
                "           <img src='https://tinypic.host/images/2024/09/21/logo.png' alt='Logo'>" +
                "       </div>" +
                "       <h1>" + errorTitle + "</h1>" +
                "       <p>" + errorMessage + "</p>" +
                "       <div class='suggestion'>" +
                "           <p>Si el problema persiste, por favor, contacta a nuestro soporte.</p>" +
                "       </div>" +
                "   </div>" +
                "</body>" +
                "</html>";
    }




}
