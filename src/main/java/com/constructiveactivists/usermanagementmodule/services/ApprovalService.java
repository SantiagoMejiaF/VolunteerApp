package com.constructiveactivists.usermanagementmodule.services;

import com.constructiveactivists.organizationmanagementmodule.services.OrganizationService;
import com.constructiveactivists.usermanagementmodule.entities.UserEntity;
import com.constructiveactivists.usermanagementmodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermanagementmodule.entities.enums.RoleType;
import com.constructiveactivists.volunteermanagementmodule.services.VolunteerService;
import com.constructiveactivists.usermanagementmodule.entities.RoleEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

        RoleType roleType = roleService.getRoleById(user.getRoleId())
                .map(RoleEntity::getRoleType)
                .orElseThrow(() -> new EntityNotFoundException("El rol con ID " + user.getRoleId() + " no existe."));

        return new StringBuilder()
                .append("Información del Usuario:\n")
                .append("Nombre: ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("\n")
                .append("Correo Electrónico: ").append(user.getEmail()).append("\n")
                .append("Fecha de Registro: ").append(user.getRegistrationDate()).append("\n")
                .append("Rol: ").append(roleType).append("\n");
    }

    private void approveUser(UserEntity user, StringBuilder userInfo) {
        user.setAuthorizationType(AuthorizationStatus.AUTORIZADO);
        userService.saveUser(user);

        String subject = "Aprobación de Acceso a VolunteerApp";
        String message = buildEmailMessage(user, userInfo, "Nos complace informarle que su solicitud de acceso " +
                "ha sido aprobada.");
        sendEmail(user.getEmail(), subject, message);
    }

    private void rejectUser(Integer userId, UserEntity user, StringBuilder userInfo) {
        deleteUserData(userId);

        String subject = "Rechazo de Acceso a VolunteerApp";
        String message = buildEmailMessage(user, userInfo, "Lamentamos informarle que su solicitud de acceso " +
                "ha sido rechazada. Toda su información ha sido eliminada de nuestro sistema.");
        sendEmail(user.getEmail(), subject, message);
    }

    private void deleteUserData(Integer userId) {
        if (isVolunteer(userId)) {
            volunteerService.deleteVolunteer(userId);
        } else if (isOrganization(userId)) {
            organizationService.deleteOrganization(userId);
        }
    }

    private boolean isVolunteer(Integer userId) {
        return volunteerService.getVolunteerById(userId).isPresent();
    }

    private boolean isOrganization(Integer userId) {
        return organizationService.getOrganizationById(userId).isPresent();
    }

    private String buildEmailMessage(UserEntity user, StringBuilder userInfo, String actionMessage) {
        return String.format("Estimado/a %s %s,\n\n%s\n\n%s\nGracias por su interés en nuestra plataforma.\n\nSaludos cordiales,\nEquipo de VolunteerApp",
                user.getFirstName(), user.getLastName(), actionMessage, userInfo.toString());
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
