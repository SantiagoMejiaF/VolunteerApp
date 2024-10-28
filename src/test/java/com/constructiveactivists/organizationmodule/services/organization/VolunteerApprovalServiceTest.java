package com.constructiveactivists.organizationmodule.services.organization;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.notificationsmodule.services.NotificationService;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.VolunteerRepository;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.VolunteerOrganizationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VolunteerApprovalServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private UserService userService;

    @Mock
    private VolunteerOrganizationService volunteerOrganizationService;

    @Mock
    private PostulationService postulationService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private VolunteerApprovalService volunteerApprovalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendVolunteerApprovalResponse_SuccessApproved() throws Exception {
        Integer volunteerId = 1;
        Integer organizationId = 2;

        VolunteerEntity volunteer = new VolunteerEntity();
        volunteer.setId(volunteerId);
        volunteer.setUserId(10);

        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(organizationId);
        organization.setOrganizationName("Test Org");

        UserEntity volunteerUser = new UserEntity();
        volunteerUser.setId(10);
        volunteerUser.setFirstName("John");
        volunteerUser.setEmail("test@example.com");

        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(100);

        PostulationEntity postulation = new PostulationEntity();
        postulation.setStatus(OrganizationStatusEnum.PENDIENTE);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(organization));
        when(userService.getUserById(volunteer.getUserId())).thenReturn(Optional.of(volunteerUser));
        when(volunteerOrganizationService.findByVolunteerIdAndOrganizationId(volunteerId, organizationId))
                .thenReturn(volunteerOrganization);
        when(postulationService.findById(volunteerOrganization.getId())).thenReturn(postulation);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        volunteerApprovalService.sendVolunteerApprovalResponse(volunteerId, organizationId, true);

        verify(organizationService, times(1)).approveVolunteer(volunteerOrganization.getId());
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(notificationService, times(1)).createNotification(volunteerUser.getId(), "Aprobación de Voluntario",
                String.format("¡Felicidades, %s! Has sido aprobado por la organización %s.", "John", "Test Org"));
    }

    @Test
    void testSendVolunteerApprovalResponse_SuccessRejected() throws Exception {
        Integer volunteerId = 1;
        Integer organizationId = 2;

        VolunteerEntity volunteer = new VolunteerEntity();
        volunteer.setId(volunteerId);
        volunteer.setUserId(10);

        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(organizationId);
        organization.setOrganizationName("Test Org");

        UserEntity volunteerUser = new UserEntity();
        volunteerUser.setId(10);
        volunteerUser.setFirstName("John");
        volunteerUser.setEmail("test@example.com");

        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(100);

        PostulationEntity postulation = new PostulationEntity();
        postulation.setStatus(OrganizationStatusEnum.PENDIENTE);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(organization));
        when(userService.getUserById(volunteer.getUserId())).thenReturn(Optional.of(volunteerUser));
        when(volunteerOrganizationService.findByVolunteerIdAndOrganizationId(volunteerId, organizationId))
                .thenReturn(volunteerOrganization);
        when(postulationService.findById(volunteerOrganization.getId())).thenReturn(postulation);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        volunteerApprovalService.sendVolunteerApprovalResponse(volunteerId, organizationId, false);

        verify(organizationService, times(1)).rejectVolunteer(volunteerOrganization.getId());
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(notificationService, times(1)).createNotification(volunteerUser.getId(), "Rechazo de Voluntario",
                String.format("Lo sentimos, %s. Has sido rechazado por la organización %s.", "John", "Test Org"));
    }

    @Test
    void testSendVolunteerApprovalResponse_VolunteerNotFound() {
        Integer volunteerId = 1;
        Integer organizationId = 2;

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                volunteerApprovalService.sendVolunteerApprovalResponse(volunteerId, organizationId, true));

        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }

    @Test
    void testSendVolunteerApprovalResponse_OrganizationNotFound() {
        Integer volunteerId = 1;
        Integer organizationId = 2;

        VolunteerEntity volunteer = new VolunteerEntity();
        volunteer.setId(volunteerId);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                volunteerApprovalService.sendVolunteerApprovalResponse(volunteerId, organizationId, true));

        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }

    @Test
    void testSendVolunteerApprovalResponse_VolunteerAlreadyRegistered() {
        Integer volunteerId = 1;
        Integer organizationId = 2;

        VolunteerEntity volunteer = new VolunteerEntity();
        volunteer.setId(volunteerId);
        volunteer.setUserId(10);

        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(organizationId);

        UserEntity volunteerUser = new UserEntity();
        volunteerUser.setId(10);
        volunteerUser.setFirstName("John");
        volunteerUser.setEmail("test@example.com");

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(organization));
        when(userService.getUserById(volunteer.getUserId())).thenReturn(Optional.of(volunteerUser));

        when(volunteerOrganizationService.findByVolunteerIdAndOrganizationId(volunteerId, organizationId))
                .thenReturn(null);

        assertThrows(BusinessException.class, () ->
                volunteerApprovalService.sendVolunteerApprovalResponse(volunteerId, organizationId, true));

        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }

    @Test
    void testSendVolunteerApprovalResponse_PostulationAlreadyProcessed() {
        Integer volunteerId = 1;
        Integer organizationId = 2;

        VolunteerEntity volunteer = new VolunteerEntity();
        volunteer.setId(volunteerId);
        volunteer.setUserId(10);

        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(organizationId);

        UserEntity volunteerUser = new UserEntity();
        volunteerUser.setId(10);
        volunteerUser.setFirstName("John");
        volunteerUser.setEmail("test@example.com");

        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(100);

        PostulationEntity postulation = new PostulationEntity();
        postulation.setStatus(OrganizationStatusEnum.ACEPTADO);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(organization));
        when(userService.getUserById(volunteer.getUserId())).thenReturn(Optional.of(volunteerUser));
        when(volunteerOrganizationService.findByVolunteerIdAndOrganizationId(volunteerId, organizationId))
                .thenReturn(volunteerOrganization);
        when(postulationService.findById(volunteerOrganization.getId())).thenReturn(postulation);

        assertThrows(BusinessException.class, () ->
                volunteerApprovalService.sendVolunteerApprovalResponse(volunteerId, organizationId, true));

        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }

    @Test
    void testSendVolunteerApprovalResponse_UserNotFound() {
        Integer volunteerId = 1;
        Integer organizationId = 2;

        VolunteerEntity volunteer = new VolunteerEntity();
        volunteer.setId(volunteerId);
        volunteer.setUserId(10);

        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(organizationId);

        when(volunteerRepository.findById(volunteerId)).thenReturn(Optional.of(volunteer));
        when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(organization));
        when(userService.getUserById(volunteer.getUserId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                volunteerApprovalService.sendVolunteerApprovalResponse(volunteerId, organizationId, true));

        assertEquals("El usuario con ID 10 no existe en la base de datos.", exception.getMessage());

        verify(mailSender, times(0)).send(any(MimeMessage.class));
    }
}
