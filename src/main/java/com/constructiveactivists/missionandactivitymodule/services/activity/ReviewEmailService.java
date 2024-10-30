package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.missionandactivitymodule.entities.activity.ActivityEntity;
import com.constructiveactivists.missionandactivitymodule.repositories.ActivityRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewEmailService {

    private final JavaMailSender mailSender;
    private final ActivityRepository activityRepository;

    public void sendFormEmail(String recipientEmail, Integer activityId) {
        Optional<ActivityEntity> activityOptional = activityRepository.findById(activityId);
        String title = activityOptional.map(ActivityEntity::getTitle).orElse("Actividad");
        String subject = "Formulario de Reseña de: "+title;
        String htmlContent = buildHtmlForm(activityId, title);
        sendHtmlEmail(recipientEmail, subject, htmlContent);
    }

    private String buildHtmlForm(Integer activityId, String activityTitle) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Formulario de Reseña</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; padding: 20px; margin: 0; }" +
                ".container { background-color: #fff; padding: 20px; border-radius: 10px; max-width: 600px; margin: auto; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }" +
                "h2 { color: #1E1450; text-align: center; }" +
                "label { font-weight: bold; display: block; margin-bottom: 5px; }" +
                "textarea { width: 100%; height: 100px; padding: 10px; margin-bottom: 20px; border-radius: 5px; border: 1px solid #ccc; }" +
                "button { background-color: #1E1450; color: #fff; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; width: 100%; }" +
                "button:hover { background-color: #3D2C8D; }" +
                "@media (max-width: 600px) {" +
                "   .container { padding: 15px; }" +
                "   h2 { font-size: 24px; }" +
                "   button { padding: 10px; }" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<img src='cid:review' alt='Review' style='display: block; margin: 0 auto; width: 150px;'>" +
                "<h2>Formulario de Reseña de: <strong>" + activityTitle + "</strong></h2>" +
                "<p>Por favor, completa el siguiente formulario para dejar tu reseña sobre la actividad.</p>" +
                "<p style='color: red; font-weight: bold;'>Nota: Una vez enviado el formulario, no se puede editar la reseña.</p>" +
                "<form action='https://volunteer-app.online/api/v1/back-volunteer-app/reviews/review' method='GET'>" +
                "<input type='hidden' name='activityId' value='" + activityId + "'/>" +
                "<label for='rating'>Por favor califica la actividad del 1 al 5, donde 5 significa que superó tus expectativas y 1 indica que fue muy insatisfactoria.</label>" +
                "<select id='rating' name='rating' required>" +
                "  <option value=''>Selecciona una calificación</option>" +
                "  <option value='1'>1 - Muy malo</option>" +
                "  <option value='2'>2 - Malo</option>" +
                "  <option value='3'>3 - Regular</option>" +
                "  <option value='4'>4 - Bueno</option>" +
                "  <option value='5'>5 - Excelente</option>" +
                "</select>" +
                "<label for='description'>Descripción:</label>" +
                "<textarea id='description' name='description' required  style=background-color: #effaff; border-left: 4px solid #06c9d7; border: 1px solid #ccc; padding: 10px;></textarea>" +
                "<button type='submit'>Enviar Reseña</button>" +
                "</form>" +
                "</div>" +
                "<p><span style='font-weight: bold; color: #000000;'><img src='cid:logo' style='height: 50px; vertical-align: middle;' alt='Volunteer App Logo' /> Volunteer App</span></p>" +
                "</body>" +
                "</html>";
    }


    void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // Añadir logo y banner al correo electrónico
            ClassPathResource logoResource = new ClassPathResource("utils/logo.png");

            helper.addInline("logo", logoResource, "image/png");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BusinessException("Fallo al enviar el correo electrónico.", e);
        }
    }
}



