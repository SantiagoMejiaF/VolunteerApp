package com.constructiveactivists.missionandactivitymodule.services.activity;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
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
        sendHtmlEmail(recipientEmail, subject, htmlContent);
    }
    private String buildHtmlForm(Integer activityId) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Formulario de Reseña</title>" +
                "<script src='com/constructiveactivists/missionandactivitymodule/services/activity/form.js'></script>" +
                "</head>" +
                "<body>" +
                "<h2>Formulario de Reseña</h2>" +
                "<form action='https://volunteer-app.online/api/v1/back-volunteer-app/reviews/review' method='GET'>" +
                "<input type='hidden' name='activityId' value='" + activityId + "'/>" +
                "<label for='description'>Descripción:</label><br>" +
                "<textarea id='description' name='description' required></textarea><br>" +
                "<label for='image'>Subir Imagen:</label><br>" +
                "<input type='file' id='image' name='image' accept='image/*' onchange='handleFileSelect(event)'><br>" +
                "<input type='hidden' id='imageBase64' name='imageBase64'/>" +
                "<button type='submit'>Enviar Reseña</button>" +
                "</form>" +
                "</body>" +
                "</html>";
    }



    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BusinessException("Fallo al enviar el correo electrónico.", e);
        }
    }
}



