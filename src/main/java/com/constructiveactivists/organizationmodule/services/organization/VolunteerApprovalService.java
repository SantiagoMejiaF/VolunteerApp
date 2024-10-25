package com.constructiveactivists.organizationmodule.services.organization;

import com.constructiveactivists.missionandactivitymodule.repositories.configurationmodule.constants.AppConstants;
import com.constructiveactivists.missionandactivitymodule.repositories.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.DataShareVolunteerOrganizationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
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
    private final VolunteerOrganizationService volunteerOrganizationService;
    private final DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;
    private final PostulationService postulationService;

    public void sendVolunteerApprovalResponse(Integer volunteerId, Integer organizationId, boolean approved) {

        VolunteerEntity volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new EntityNotFoundException("El voluntario con ID " + volunteerId + AppConstants.NOT_FOUND_MESSAGE));
        OrganizationEntity organization = organizationService.getOrganizationById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("La organización con ID " + organizationId +  AppConstants.NOT_FOUND_MESSAGE));

        UserEntity volunteerUser = userService.getUserById(volunteer.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + volunteer.getUserId() +  AppConstants.NOT_FOUND_MESSAGE));

        VolunteerOrganizationEntity volunteerOrganizationInstance = volunteerOrganizationService.findByVolunteerIdAndOrganizationId(volunteerId, organizationId);

        if (volunteerOrganizationInstance == null) {
            throw new BusinessException("El voluntario ya está registrado en la organización");
        }
        PostulationEntity postulation = postulationService.findById(volunteerOrganizationInstance.getId());

        if(!postulation.getStatus().equals(OrganizationStatusEnum.PENDIENTE)) {
            throw new BusinessException("La solicitud ya ha sido procesada");
        }

        String volunteerEmail = volunteerUser.getEmail();
        String volunteerName = volunteerUser.getFirstName();
        String organizationName = organization.getOrganizationName();

        if (approved) {
            organizationService.approveVolunteer(volunteerOrganizationInstance.getId());
            sendApprovalEmail(volunteerEmail, organizationName, volunteerName);
        } else {
            organizationService.rejectVolunteer(volunteerOrganizationInstance.getId());
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

    void sendHtmlEmail(String to, String subject, String htmlContent, String imageName) {
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
            throw new BusinessException("Error al enviar el correo electrónico", e);
        }
    }
}
