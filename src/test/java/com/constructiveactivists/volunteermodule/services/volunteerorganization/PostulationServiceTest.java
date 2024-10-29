package com.constructiveactivists.volunteermodule.services.volunteerorganization;

import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.PostulationRepository;
import com.constructiveactivists.volunteermodule.repositories.VolunteerOrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostulationServiceTest {

    @Mock
    private PostulationRepository postulationRepository;

    @Mock
    private VolunteerOrganizationRepository volunteerOrganizationRepository;

    @Mock
    private DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    @InjectMocks
    private PostulationService postulationService;

    private PostulationEntity postulationEntity;
    private VolunteerOrganizationEntity volunteerOrganizationEntity;
    private DataShareVolunteerOrganizationEntity dataShareEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        postulationEntity = new PostulationEntity();
        postulationEntity.setVolunteerOrganizationId(1);
        postulationEntity.setStatus(OrganizationStatusEnum.PENDIENTE);
        postulationEntity.setRegistrationDate(LocalDate.now());

        volunteerOrganizationEntity = new VolunteerOrganizationEntity();
        volunteerOrganizationEntity.setId(1);
        volunteerOrganizationEntity.setVolunteerId(1);
        volunteerOrganizationEntity.setOrganizationId(1);

        dataShareEntity = new DataShareVolunteerOrganizationEntity();
        dataShareEntity.setVolunteerOrganizationId(1);
        dataShareEntity.setHoursDone(0);
        dataShareEntity.setHoursCertified(0);
        dataShareEntity.setMonthlyHours(0);
    }

    @Test
    void testAddPostulationPending_Success() {
        when(postulationRepository.save(any(PostulationEntity.class))).thenReturn(postulationEntity);

        PostulationEntity result = postulationService.addPostulationPending(1);

        assertNotNull(result);
        assertEquals(OrganizationStatusEnum.PENDIENTE, result.getStatus());
        verify(postulationRepository, times(1)).save(any(PostulationEntity.class));
    }

    @Test
    void testUpdateStatusAccept_Success() {
        when(postulationRepository.findById(1)).thenReturn(Optional.of(postulationEntity));

        postulationService.updateStatusAccept(1);

        assertEquals(OrganizationStatusEnum.ACEPTADO, postulationEntity.getStatus());
        verify(postulationRepository, times(1)).save(postulationEntity);
    }

    @Test
    void testUpdateStatusRefuse_Success() {
        when(postulationRepository.findById(1)).thenReturn(Optional.of(postulationEntity));

        postulationService.updateStatusRefuse(1);

        assertEquals(OrganizationStatusEnum.RECHAZADO, postulationEntity.getStatus());
        verify(postulationRepository, times(1)).save(postulationEntity);
    }

    @Test
    void testGetPendingPostulationsByOrganizationId_Success() {
        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(Collections.singletonList(volunteerOrganizationEntity));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(eq(OrganizationStatusEnum.PENDIENTE), anyList()))
                .thenReturn(Collections.singletonList(postulationEntity));

        List<PostulationEntity> result = postulationService.getPendingPostulationsByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals(OrganizationStatusEnum.PENDIENTE, result.get(0).getStatus());
        verify(postulationRepository, times(1))
                .findByStatusAndVolunteerOrganizationIdIn(eq(OrganizationStatusEnum.PENDIENTE), anyList());
    }

    @Test
    void testGetAcceptedPostulationsByOrganizationId_Success() {

        postulationEntity.setStatus(OrganizationStatusEnum.ACEPTADO);

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(Collections.singletonList(volunteerOrganizationEntity));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(eq(OrganizationStatusEnum.ACEPTADO), anyList()))
                .thenReturn(Collections.singletonList(postulationEntity));

        List<PostulationEntity> result = postulationService.getAcceptedPostulationsByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals(OrganizationStatusEnum.ACEPTADO, result.get(0).getStatus());
        verify(postulationRepository, times(1))
                .findByStatusAndVolunteerOrganizationIdIn(eq(OrganizationStatusEnum.ACEPTADO), anyList());
    }

    @Test
    void testAddTimeToPostulation_Success() {
        when(postulationRepository.findById(1)).thenReturn(Optional.of(postulationEntity));
        when(dataShareVolunteerOrganizationService.findById(1)).thenReturn(dataShareEntity);

        postulationService.addTimeToPostulation(1, 10);

        assertEquals(10, dataShareEntity.getHoursDone());
        assertEquals(10, dataShareEntity.getHoursCertified());
        verify(dataShareVolunteerOrganizationService, times(1)).findById(1);
        verify(postulationRepository, times(1)).save(postulationEntity);
    }

    @Test
    void testFindById_Success() {
        when(postulationRepository.findById(1)).thenReturn(Optional.of(postulationEntity));

        PostulationEntity result = postulationService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getVolunteerOrganizationId());
        verify(postulationRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_EntityNotFound() {
        when(postulationRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            postulationService.findById(1);
        });

        assertEquals("Postulación no encontrada1", exception.getMessage());
        verify(postulationRepository, times(1)).findById(1);
    }

    @Test
    void testGetRefusedPostulationsByOrganizationId_WithPostulations() {
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(1);
        volunteerOrganization.setOrganizationId(1);

        PostulationEntity refusedPostulation = new PostulationEntity();
        refusedPostulation.setVolunteerOrganizationId(1);
        refusedPostulation.setStatus(OrganizationStatusEnum.RECHAZADO);

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(List.of(volunteerOrganization));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.RECHAZADO, List.of(1)))
                .thenReturn(List.of(refusedPostulation));

        List<PostulationEntity> result = postulationService.getRefusedPostulationsByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals(OrganizationStatusEnum.RECHAZADO, result.get(0).getStatus());
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
        verify(postulationRepository, times(1))
                .findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.RECHAZADO, List.of(1));
    }

    @Test
    void testGetRefusedPostulationsByOrganizationId_NoPostulations() {
        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(Collections.emptyList());

        List<PostulationEntity> result = postulationService.getRefusedPostulationsByOrganizationId(1);

        assertTrue(result.isEmpty());
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
        verify(postulationRepository, never()).findByStatusAndVolunteerOrganizationIdIn(any(), any());
    }

    @Test
    void testGetPostulationsByDateRange() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        PostulationEntity postulation = new PostulationEntity();
        postulation.setRegistrationDate(startDate);

        when(postulationRepository.findAllByRegistrationDateBetween(startDate, endDate))
                .thenReturn(List.of(postulation));

        List<PostulationEntity> result = postulationService.getPostulationsByDateRange(startDate, endDate);

        assertEquals(1, result.size());
        assertEquals(startDate, result.get(0).getRegistrationDate());
        verify(postulationRepository, times(1)).findAllByRegistrationDateBetween(startDate, endDate);
    }

    @Test
    void testFindAllPostulations() {
        PostulationEntity postulation1 = new PostulationEntity();
        PostulationEntity postulation2 = new PostulationEntity();

        when(postulationRepository.findAll()).thenReturn(List.of(postulation1, postulation2));

        List<PostulationEntity> result = postulationService.findAll();

        assertEquals(2, result.size());
        verify(postulationRepository, times(1)).findAll();
    }



    @Test
    void testGetPendingPostulationsByOrganizationId_NoVolunteerOrganizations() {
        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(Collections.emptyList());

        List<PostulationEntity> result = postulationService.getPendingPostulationsByOrganizationId(1);

        assertTrue(result.isEmpty());
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
        verify(postulationRepository, never()).findByStatusAndVolunteerOrganizationIdIn(any(), any());
    }

    @Test
    void testGetPendingPostulationsByOrganizationId_WithPendingPostulations() {
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(1);
        volunteerOrganization.setOrganizationId(1);

        PostulationEntity pendingPostulation = new PostulationEntity();
        pendingPostulation.setVolunteerOrganizationId(1);
        pendingPostulation.setStatus(OrganizationStatusEnum.PENDIENTE);

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(List.of(volunteerOrganization));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.PENDIENTE, List.of(1)))
                .thenReturn(List.of(pendingPostulation));

        List<PostulationEntity> result = postulationService.getPendingPostulationsByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals(OrganizationStatusEnum.PENDIENTE, result.get(0).getStatus());
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
        verify(postulationRepository, times(1))
                .findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.PENDIENTE, List.of(1));
    }

    @Test
    void testGetAcceptedPostulationsByOrganizationId_NoVolunteerOrganizations() {
        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(Collections.emptyList());

        List<PostulationEntity> result = postulationService.getAcceptedPostulationsByOrganizationId(1);

        assertTrue(result.isEmpty());
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
        verify(postulationRepository, never()).findByStatusAndVolunteerOrganizationIdIn(any(), any());
    }

    @Test
    void testGetAcceptedPostulationsByOrganizationId_WithAcceptedPostulations() {
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(1);
        volunteerOrganization.setOrganizationId(1);

        PostulationEntity acceptedPostulation = new PostulationEntity();
        acceptedPostulation.setVolunteerOrganizationId(1);
        acceptedPostulation.setStatus(OrganizationStatusEnum.ACEPTADO);

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(List.of(volunteerOrganization));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.ACEPTADO, List.of(1)))
                .thenReturn(List.of(acceptedPostulation));

        List<PostulationEntity> result = postulationService.getAcceptedPostulationsByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals(OrganizationStatusEnum.ACEPTADO, result.get(0).getStatus());
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
        verify(postulationRepository, times(1))
                .findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.ACEPTADO, List.of(1));
    }
}
