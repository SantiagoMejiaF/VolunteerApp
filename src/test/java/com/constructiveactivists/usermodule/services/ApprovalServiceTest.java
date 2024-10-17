package com.constructiveactivists.usermodule.services;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.enums.AuthorizationStatus;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.services.volunteer.VolunteerService;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApprovalServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private VolunteerService volunteerService;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private ApprovalService approvalService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setAuthorizationType(AuthorizationStatus.PENDIENTE);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1);
        roleEntity.setRoleType(RoleType.VOLUNTARIO);
        userEntity.setRole(roleEntity);

        approvalService = new ApprovalService(mailSender, userService, roleService, volunteerService, organizationService);

    }

    @Test
    void testSendApprovalResponse_UserNotPending() {
        userEntity.setAuthorizationType(AuthorizationStatus.AUTORIZADO);
        when(userService.getUserById(1)).thenReturn(Optional.of(userEntity));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            approvalService.sendApprovalResponse(1, true);
        });

        assertEquals("El estado de autorización del usuario no es PENDIENTE. No se puede enviar el correo.", exception.getMessage());
    }

    @Test
    void testSendApprovalResponse_UserNotFound() {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            approvalService.sendApprovalResponse(1, true);
        });

        assertEquals("El usuario con ID 1 no existe.", exception.getMessage());
    }

    @Test
    void testSendApprovalResponse_RoleNotFound() {
        when(userService.getUserById(1)).thenReturn(Optional.of(userEntity));
        when(roleService.getRoleById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            approvalService.sendApprovalResponse(1, true);
        });

        assertEquals("El rol con ID 1 no existe.", exception.getMessage());
    }

    @Test
    void testSendHtmlEmail_Success() {

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(userService.getUserById(1)).thenReturn(Optional.of(userEntity));

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1);
        roleEntity.setRoleType(RoleType.ORGANIZACION);

        when(roleService.getRoleById(1)).thenReturn(Optional.of(roleEntity));

        approvalService.sendApprovalResponse(1, true);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testDeleteUserData_VolunteerSuccess() {
        Integer userId = 1;
        VolunteerEntity volunteer = new VolunteerEntity();
        volunteer.setId(1);

        when(volunteerService.getVolunteerByUserId(userId)).thenReturn(Optional.of(volunteer));

        approvalService.deleteUserData(userId);

        verify(volunteerService, times(1)).deleteVolunteer(volunteer.getId());
        verify(organizationService, times(0)).deleteOrganization(anyInt());
    }

    @Test
    void testDeleteUserData_OrganizationSuccess() {
        Integer userId = 2;
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(2);

        when(volunteerService.getVolunteerByUserId(userId)).thenReturn(Optional.empty());
        when(organizationService.getOrganizationByUserId(userId)).thenReturn(Optional.of(organization));

        approvalService.deleteUserData(userId);

        verify(organizationService, times(1)).deleteOrganization(organization.getId());
        verify(volunteerService, times(0)).deleteVolunteer(anyInt());
    }

    @Test
    void testDeleteUserData_UserNotFound() {
        Integer userId = 3;

        when(volunteerService.getVolunteerByUserId(userId)).thenReturn(Optional.empty());
        when(organizationService.getOrganizationByUserId(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            approvalService.deleteUserData(userId);
        });

        assertEquals("El usuario con ID 3 no es un voluntario ni una organización.", exception.getMessage());
    }

    @Test
    void testIsVolunteer_True() {
        Integer userId = 1;
        when(volunteerService.getVolunteerByUserId(userId)).thenReturn(Optional.of(new VolunteerEntity()));

        assertTrue(approvalService.isVolunteer(userId));
    }

    @Test
    void testIsVolunteer_False() {
        Integer userId = 2;
        when(volunteerService.getVolunteerByUserId(userId)).thenReturn(Optional.empty());

        assertFalse(approvalService.isVolunteer(userId));
    }

    @Test
    void testIsOrganization_True() {
        Integer userId = 1;
        when(organizationService.getOrganizationByUserId(userId)).thenReturn(Optional.of(new OrganizationEntity()));

        assertTrue(approvalService.isOrganization(userId));
    }

    @Test
    void testIsOrganization_False() {
        Integer userId = 2;
        when(organizationService.getOrganizationByUserId(userId)).thenReturn(Optional.empty());

        assertFalse(approvalService.isOrganization(userId));
    }

    @Test
    void testSendApprovalResponse_RejectUser() {
        Integer userId = 1;

        userEntity.setAuthorizationType(AuthorizationStatus.PENDIENTE);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1);
        roleEntity.setRoleType(RoleType.VOLUNTARIO);

        when(userService.getUserById(userId)).thenReturn(Optional.of(userEntity));
        when(roleService.getRoleById(1)).thenReturn(Optional.of(roleEntity));

        when(volunteerService.getVolunteerByUserId(userId)).thenReturn(Optional.empty());
        when(organizationService.getOrganizationByUserId(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            approvalService.sendApprovalResponse(userId, false);
        });

        assertEquals("El usuario con ID 1 no es un voluntario ni una organización.", exception.getMessage());

        verify(volunteerService, times(1)).getVolunteerByUserId(userId);
        verify(organizationService, times(1)).getOrganizationByUserId(userId);
    }
}
