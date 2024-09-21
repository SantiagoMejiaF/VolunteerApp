package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewEmailService {

    private final JavaMailSender mailSender;

    public void sendFormEmail(String recipientEmail, Integer activityId) {
        String subject = "Formulario de Reseña";
        String htmlContent = buildHtmlForm(activityId);
        sendHtmlEmail(recipientEmail, subject, htmlContent, "correo.png");
    }

    private String buildHtmlForm(Integer activityId) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Formulario de Reseña</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; padding: 20px; }" +
                ".container { background-color: #fff; padding: 20px; border-radius: 10px; max-width: 600px; margin: auto; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }" +
                "h2 { color: #1E1450; text-align: center; }" +
                "label { font-weight: bold; }" +
                "textarea { width: 100%; height: 100px; padding: 10px; margin-bottom: 20px; border-radius: 5px; border: 1px solid #ccc; }" +
                "button { background-color: #1E1450; color: #fff; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; }" +
                "button:hover { background-color: #3D2C8D; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<img src='cid:logo' alt='Logo' style='display: block; margin: 0 auto; width: 150px;'>" +
                "<h2>Formulario de Reseña</h2>" +
                "<p>Por favor, completa el siguiente formulario para dejar tu reseña sobre la actividad.</p>" +
                "<form action='https://volunteer-app.online/api/v1/back-volunteer-app/reviews/review' method='POST'>" +
                "<input type='hidden' name='activityId' value='" + activityId + "'/>" +
                "<label for='description'>Descripción:</label><br>" +
                "<textarea id='description' name='description' required></textarea><br>" +
                "<button type='submit'>Enviar Reseña</button>" +
                "</form>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent, String imageName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // Añadir logo y banner al correo electrónico
            ClassPathResource logoResource = new ClassPathResource("utils/logo.png");
            ClassPathResource bannerResource = new ClassPathResource("utils/" + imageName);

            helper.addInline("logo", logoResource, "image/png");
            helper.addInline("correo", bannerResource, "image/png");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BusinessException("Fallo al enviar el correo electrónico.", e);
        }
    }
}



