package com.constructiveactivists.organizationmanagementmodule.services;

import com.constructiveactivists.authenticationmodule.controllers.configuration.constants.AppConstants;
import com.constructiveactivists.authenticationmodule.controllers.configuration.exceptions.BusinessException;
import com.constructiveactivists.organizationmanagementmodule.entities.OrganizationEntity;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.services.UserService;
import com.constructiveactivists.volunteermanagementmodule.entities.VolunteerEntity;
import com.constructiveactivists.volunteermanagementmodule.repositories.VolunteerRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VolunteerApprovalService {

    private final JavaMailSender mailSender;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationService organizationService;
    private final UserService userService;

    public void sendVolunteerApprovalResponse(Integer volunteerId, Integer organizationId, boolean approved) {

        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new EntityNotFoundException("El voluntario con ID " + volunteerId + " " + AppConstants.NOT_FOUND_MESSAGE));
        OrganizationEntity organization = organizationService.getOrganizationById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("La organización con ID " + organizationId + " " + AppConstants.NOT_FOUND_MESSAGE));

        UserEntity volunteerUser = userService.getUserById(volunteer.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + volunteer.getUserId() + " " + AppConstants.NOT_FOUND_MESSAGE));

        String volunteerEmail = volunteerUser.getEmail();
        String volunteerName = volunteerUser.getFirstName();
        String organizationName = organization.getOrganizationName();

        if (approved) {
            organizationService.approveVolunteer(volunteerId, organizationId);
            sendApprovalEmail(volunteerEmail, organizationName, volunteerName);
        } else {
            sendRejectionEmail(volunteerEmail, organizationName, volunteerName);
        }
    }

    private void sendApprovalEmail(String volunteerEmail, String organizationName, String volunteerName) {
        String subject = String.format(AppConstants.APPROVAL_SUBJECT, organizationName);
        String message = buildHtmlEmailMessage(volunteerName, organizationName, AppConstants.APPROVAL_MESSAGE, true);
        sendHtmlEmail(volunteerEmail, subject, message, "request-accepted.png");
    }

    private void sendRejectionEmail(String volunteerEmail, String organizationName, String volunteerName) {
        String subject = String.format(AppConstants.REJECTION_SUBJECT, organizationName);
        String message = buildHtmlEmailMessage(volunteerName, organizationName, AppConstants.REJECTION_MESSAGE, false);
        sendHtmlEmail(volunteerEmail, subject, message, "request-rejected.png");
    }

    private String buildHtmlEmailMessage(String volunteerName, String organizationName, String actionMessage, boolean approved) {
        String backgroundColor = approved ? "#1E1450" : "#ED4B4B";
        String statusMessage = approved ? "APROBADO" : "RECHAZADO";

        return String.format(
                AppConstants.EMAIL_TEMPLATE,
                backgroundColor, statusMessage,
                volunteerName, actionMessage, organizationName
        );
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent, String imageName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            ClassPathResource imageResource = new ClassPathResource("utils/" + imageName);
            ClassPathResource logoResource = new ClassPathResource("utils/logo.png");

            helper.addInline("image", imageResource, "image/png");
            helper.addInline("logo", logoResource, "image/png");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BusinessException("Fallo al enviar el correo electrónico.", e);
        }
    }
}
