package com.constructiveactivists.usermodule.services;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;

import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import com.constructiveactivists.usermodule.entities.RoleEntity;
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
public class ApprovalService {

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final RoleService roleService;
    private final VolunteerService volunteerService;
    private final OrganizationService organizationService;

    public void sendApprovalResponse(Integer userId, boolean approved) {
        UserEntity user = getUser(userId);
        if (user.getAuthorizationType() != AuthorizationStatus.PENDIENTE) {
            throw new BusinessException("El estado de autorización del usuario no es PENDIENTE. No se puede enviar el correo.");
        }
        StringBuilder userInfo = buildUserInfo(user);
        if (approved) {
            approveUser(user, userInfo);
        } else {
            rejectUser(userId, user, userInfo);
        }
    }

    private UserEntity getUser(Integer userId) {
        return userService.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("El usuario con ID " + userId + " no existe."));
    }

    private StringBuilder buildUserInfo(UserEntity user) {
        RoleType roleType = roleService.getRoleById(user.getRole().getId())
                .map(RoleEntity::getRoleType)
                .orElseThrow(() -> new EntityNotFoundException("El rol con ID " + user.getRole().getId() + " no existe."));

        return new StringBuilder()
                .append("<div style='color: #000000;'>")
                .append("Nombre del usuario: ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("<br>")
                .append("Correo: ").append(user.getEmail()).append("<br>")
                .append("Fecha Registro: ").append(user.getRegistrationDate()).append("<br>")
                .append("Rol: ").append(roleType).append("<br>")
                .append("</div>");
    }

    private void approveUser(UserEntity user, StringBuilder userInfo) {
        user.setAuthorizationType(AuthorizationStatus.AUTORIZADO);
        userService.saveUser(user);

        String subject = "Aprobación de Acceso a VolunteerApp";
        String htmlMessage = buildHtmlEmailMessage(user, userInfo, "Nos complace informarle que su solicitud de acceso ha sido aprobada.", true);
        sendHtmlEmail(user.getEmail(), subject, htmlMessage, "request-accepted.png");
    }

    void rejectUser(Integer userId, UserEntity user, StringBuilder userInfo) {
        deleteUserData(userId);

        String subject = "Rechazo de Acceso a VolunteerApp";
        String htmlMessage = buildHtmlEmailMessage(user, userInfo, "Lamentamos informarle que su solicitud de acceso ha sido rechazada. Toda su información ha sido eliminada de nuestro sistema.", false);
        sendHtmlEmail(user.getEmail(), subject, htmlMessage, "request-rejected.png");
    }

    private String buildHtmlEmailMessage(UserEntity user, StringBuilder userInfo, String actionMessage, boolean approved) {
        String backgroundColor = approved ? "#4abac8" : "#Ef8770";
        String rejectionMessage = "Queremos que sepas que esto no es un adiós, sino un hasta pronto. Si tienes alguna duda comunícate con nosotros.";
        return String.format(
                "<html>" +
                        "<body style='font-family: Inter, sans-serif; color: #000000;'>" +
                        "<div style='background-color: %s; padding: 30px 10px; text-align: center; color: white;'>" +
                        "<img src='cid:image' style='height: 150px; vertical-align: middle; margin-right: 20px;' alt='Volunteer App' />" +
                        "<span style='font-size: 28px; font-weight: bold;'>%s</span>" +
                        "</div>" +
                        "<p>Estimado/a %s %s,</p>" +
                        "<p style='color: #000000;'>%s</p>" +
                        "<h3 style='color: #306e86; font-weight: bold;'>Aquí puedes ver los detalles de tus datos:</h3>" +
                        "<div style='background-color: #dddfe4; padding: 10px; border-radius: 5px;'>%s</div>" +
                        "<p style='color: #000000;'>Un abrazo,</p>" +
                        "%s" +
                        "<p><span style='font-weight: bold; color: #000000;'><img src='cid:logo' style='height: 50px; vertical-align: middle;' alt='Volunteer App Logo' /> Volunteer App</span></p>" +
                        "</body>" +
                        "</html>",
                backgroundColor,
                approved ? "ACEPTADO" : "RECHAZADO",
                user.getFirstName(),
                user.getLastName(),
                actionMessage,
                userInfo.toString(),
                approved ? "" : "<p style='color: #000000;'>" + rejectionMessage + "</p>"
        );

    }

    void sendHtmlEmail(String to, String subject, String htmlContent, String imageName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            ClassPathResource imageResource = new ClassPathResource("utils/"+ imageName);
            ClassPathResource logoResource = new ClassPathResource("utils/logo.png");

            helper.addInline("image", imageResource, "image/png");
            helper.addInline("logo", logoResource, "image/png");


            mailSender.send(message);
        } catch (MessagingException e) {
            throw new BusinessException("Fallo al enviar el correo electrónico.", e);
        }
    }

    void deleteUserData(Integer userId) {
        if (isVolunteer(userId)) {
            volunteerService.getVolunteerByUserId(userId)
                    .ifPresent(volunteer -> volunteerService.deleteVolunteer(volunteer.getId()));
        } else if (isOrganization(userId)) {
            organizationService.getOrganizationByUserId(userId)
                    .ifPresent(organization -> organizationService.deleteOrganization(organization.getId()));
        } else{
            throw new EntityNotFoundException("El usuario con ID " + userId + " no es un voluntario ni una organización.");
        }
    }

    boolean isVolunteer(Integer userId) {
        return volunteerService.getVolunteerByUserId(userId).isPresent();
    }

    boolean isOrganization(Integer userId) {
        return organizationService.getOrganizationByUserId(userId).isPresent();
    }
}
