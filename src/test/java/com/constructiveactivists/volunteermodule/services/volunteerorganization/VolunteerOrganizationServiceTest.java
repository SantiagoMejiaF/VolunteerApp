package com.constructiveactivists.volunteermodule.services.volunteerorganization;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.repositories.UserRepository;
import com.constructiveactivists.volunteermodule.controllers.response.StatusVolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.controllers.response.VolunteerOrganizationResponse;
import com.constructiveactivists.volunteermodule.entities.volunteer.PersonalInformationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.VolunteerEntity;
import com.constructiveactivists.volunteermodule.entities.volunteer.enums.OrganizationStatusEnum;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.PostulationEntity;
import com.constructiveactivists.volunteermodule.entities.volunteerorganization.VolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VolunteerOrganizationServiceTest {

    @Mock
    private VolunteerOrganizationRepository volunteerOrganizationRepository;

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private PostulationRepository postulationRepository;

    @Mock
    private DataShareVolunteerOrganizationRepository dataShareRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostulationService postulationService;

    @Mock
    private DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    @InjectMocks
    private VolunteerOrganizationService volunteerOrganizationService;

    private VolunteerOrganizationEntity volunteerOrganization;

    private PostulationEntity postulation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(1);
        volunteerOrganization.setVolunteerId(1);
        volunteerOrganization.setOrganizationId(1);

        postulation = new PostulationEntity();
        postulation.setVolunteerOrganizationId(1);
        postulation.setStatus(OrganizationStatusEnum.ACEPTADO);
    }

    @Test
    void testSaveVolunteerOrganization_Success() {
        when(volunteerRepository.existsById(1)).thenReturn(true);
        when(organizationRepository.existsById(1)).thenReturn(true);
        when(volunteerOrganizationRepository.save(any(VolunteerOrganizationEntity.class))).thenReturn(volunteerOrganization);

        VolunteerOrganizationEntity result = volunteerOrganizationService.save(volunteerOrganization);

        assertNotNull(result);
        assertEquals(1, result.getVolunteerId());
        verify(volunteerRepository, times(1)).existsById(1);
        verify(organizationRepository, times(1)).existsById(1);
        verify(volunteerOrganizationRepository, times(1)).save(volunteerOrganization);
    }

    @Test
    void testGetVolunteerOrganizationDetails_Success() {
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganization));
        when(postulationRepository.findById(1)).thenReturn(Optional.of(new PostulationEntity()));
        when(dataShareRepository.findById(1)).thenReturn(Optional.of(new DataShareVolunteerOrganizationEntity()));
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(new VolunteerEntity()));

        VolunteerOrganizationResponse result = volunteerOrganizationService.getVolunteerOrganizationDetails(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(volunteerOrganizationRepository, times(1)).findById(1);
        verify(postulationRepository, times(1)).findById(1);
        verify(dataShareRepository, times(1)).findById(1);
        verify(volunteerRepository, times(1)).findById(1);
    }

    @Test
    void testGetPendingVolunteersByOrganizationId() {
        volunteerOrganization.setId(1);
        volunteerOrganization.setVolunteerId(1);

        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        PostulationEntity postulationEntity = new PostulationEntity();
        postulationEntity.setVolunteerOrganizationId(1);
        postulationEntity.setStatus(OrganizationStatusEnum.PENDIENTE);
        List<PostulationEntity> postulations = List.of(postulationEntity);

        VolunteerEntity volunteerEntity = new VolunteerEntity();
        volunteerEntity.setUserId(1);
        volunteerEntity.setPersonalInformation(new PersonalInformationEntity());

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(volunteerOrganizations);
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganization));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.PENDIENTE, List.of(1))).thenReturn(postulations);
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        List<StatusVolunteerOrganizationResponse> result = volunteerOrganizationService.getPendingVolunteersByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
    }


    @Test
    void testGetAcceptedVolunteersByOrganizationId() {
        volunteerOrganization.setVolunteerId(1);

        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        PostulationEntity postulationEntity = new PostulationEntity();
        postulationEntity.setVolunteerOrganizationId(1);
        postulationEntity.setStatus(OrganizationStatusEnum.ACEPTADO);
        List<PostulationEntity> postulations = List.of(postulationEntity);

        VolunteerEntity volunteerEntity = new VolunteerEntity();
        volunteerEntity.setUserId(1);
        volunteerEntity.setPersonalInformation(new PersonalInformationEntity());

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(volunteerOrganizations);
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganization));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.ACEPTADO, List.of(1))).thenReturn(postulations);
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        List<StatusVolunteerOrganizationResponse> result = volunteerOrganizationService.getAcceptedVolunteersByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
    }

    @Test
    void testGetRejectedVolunteersByOrganizationId() {
        volunteerOrganization.setId(1);
        volunteerOrganization.setVolunteerId(1);

        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        PostulationEntity postulationEntity = new PostulationEntity();
        postulationEntity.setVolunteerOrganizationId(1);
        postulationEntity.setStatus(OrganizationStatusEnum.RECHAZADO);
        List<PostulationEntity> postulations = List.of(postulationEntity);

        VolunteerEntity volunteerEntity = new VolunteerEntity();
        volunteerEntity.setUserId(1);
        volunteerEntity.setPersonalInformation(new PersonalInformationEntity());

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(volunteerOrganizations);
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganization));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.RECHAZADO, List.of(1))).thenReturn(postulations);
        when(volunteerRepository.findById(1)).thenReturn(Optional.of(volunteerEntity));
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        List<StatusVolunteerOrganizationResponse> result = volunteerOrganizationService.getRejectedVolunteersByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFullName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());
    }

    @Test
    void testGetOrganizationIdsByVolunteerId() {
        volunteerOrganization.setOrganizationId(100);

        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        when(volunteerOrganizationRepository.findByVolunteerId(1)).thenReturn(volunteerOrganizations);

        List<Integer> result = volunteerOrganizationService.getOrganizationIdsByVolunteerId(1);

        assertEquals(1, result.size());
        assertEquals(100, result.get(0));

        verify(volunteerOrganizationRepository, times(1)).findByVolunteerId(1);
    }

    @Test
    void testAddVolunteerOrganizationPending_Success() {
        volunteerOrganization.setVolunteerId(1);
        volunteerOrganization.setOrganizationId(1);

        when(volunteerOrganizationRepository.existsByVolunteerIdAndOrganizationId(1, 1)).thenReturn(false);
        when(volunteerRepository.existsById(1)).thenReturn(true);
        when(organizationRepository.existsById(1)).thenReturn(true);
        when(volunteerOrganizationRepository.save(volunteerOrganization)).thenReturn(volunteerOrganization);

        VolunteerOrganizationEntity result = volunteerOrganizationService.addVolunteerOrganizationPending(volunteerOrganization);

        assertNotNull(result);
        verify(volunteerOrganizationRepository, times(1)).save(volunteerOrganization);
        verify(postulationService, times(1)).addPostulationPending(volunteerOrganization.getId());
        verify(dataShareVolunteerOrganizationService, times(1)).addDataShareVolunteerOrganization(volunteerOrganization.getId());
    }

    @Test
    void testAddVolunteerOrganizationPending_AlreadyExists() {
        volunteerOrganization.setVolunteerId(1);
        volunteerOrganization.setOrganizationId(1);

        when(volunteerOrganizationRepository.existsByVolunteerIdAndOrganizationId(1, 1)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerOrganizationService.addVolunteerOrganizationPending(volunteerOrganization);
        });

        assertEquals("El voluntario ya está registrado en la organización", exception.getMessage());
        verify(volunteerOrganizationRepository, never()).save(volunteerOrganization);
    }

    @Test
    void testFindByVolunteerIdAndOrganizationId_Success() {
        when(volunteerOrganizationRepository.findByVolunteerIdAndOrganizationId(1, 1)).thenReturn(Optional.of(volunteerOrganization));

        VolunteerOrganizationEntity result = volunteerOrganizationService.findByVolunteerIdAndOrganizationId(1, 1);

        assertNotNull(result);
        verify(volunteerOrganizationRepository, times(1)).findByVolunteerIdAndOrganizationId(1, 1);
    }

    @Test
    void testFindByVolunteerIdAndOrganizationId_NotFound() {
        when(volunteerOrganizationRepository.findByVolunteerIdAndOrganizationId(1, 1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerOrganizationService.findByVolunteerIdAndOrganizationId(1, 1);
        });

        assertEquals("La relación entre el voluntario con ID 1 y la organización con ID 1 no se encuentra.", exception.getMessage());
        verify(volunteerOrganizationRepository, times(1)).findByVolunteerIdAndOrganizationId(1, 1);
    }

    @Test
    void testExistsByVolunteerIdAndOrganizationId() {
        when(volunteerOrganizationRepository.existsByVolunteerIdAndOrganizationId(1, 1)).thenReturn(true);

        boolean result = volunteerOrganizationService.existsByVolunteerIdAndOrganizationId(1, 1);

        assertTrue(result);
        verify(volunteerOrganizationRepository, times(1)).existsByVolunteerIdAndOrganizationId(1, 1);
    }

    @Test
    void testGetOrganizationsByVolunteerId() {
        volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(1);
        volunteerOrganization.setVolunteerId(1);
        volunteerOrganization.setOrganizationId(1);

        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        when(volunteerOrganizationRepository.findByVolunteerId(1)).thenReturn(volunteerOrganizations);

        postulation = new PostulationEntity();
        postulation.setVolunteerOrganizationId(volunteerOrganization.getId());
        postulation.setStatus(OrganizationStatusEnum.ACEPTADO);

        when(postulationRepository.findById(volunteerOrganization.getId())).thenReturn(Optional.of(postulation));

        List<VolunteerOrganizationEntity> result = volunteerOrganizationService.getOrganizationsByVolunteerId(1);

        assertEquals(1, result.size(), "Se espera una sola organización aceptada para el voluntario.");
        verify(volunteerOrganizationRepository, times(1)).findByVolunteerId(1);
        verify(postulationRepository, times(1)).findById(volunteerOrganization.getId());
    }

    @Test
    void testGetVolunteersByOrganizationId() {
        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(volunteerOrganizations);

        List<VolunteerOrganizationEntity> result = volunteerOrganizationService.getVolunteersByOrganizationId(1);

        assertEquals(1, result.size());
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
    }

    @Test
    void testFindAll() {
        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        when(volunteerOrganizationRepository.findAll()).thenReturn(volunteerOrganizations);

        List<VolunteerOrganizationEntity> result = volunteerOrganizationService.findAll();

        assertEquals(1, result.size());
        verify(volunteerOrganizationRepository, times(1)).findAll();
    }

    @Test
    void testFindVolunteerOrganizationIdsByOrganizationId() {
        volunteerOrganization.setId(1);

        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(volunteerOrganizations);

        List<Integer> result = volunteerOrganizationService.findVolunteerOrganizationIdsByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0));
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
    }

    @Test
    void testSaveVolunteerOrganization_VolunteerNotFound() {
        when(volunteerRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerOrganizationService.save(volunteerOrganization);
        });

        assertEquals("El voluntario con ID 1 no existe.", exception.getMessage());
        verify(volunteerRepository, times(1)).existsById(1);
        verify(organizationRepository, never()).existsById(anyInt());
    }

    @Test
    void testSaveVolunteerOrganization_OrganizationNotFound() {
        when(volunteerRepository.existsById(1)).thenReturn(true);
        when(organizationRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            volunteerOrganizationService.save(volunteerOrganization);
        });

        assertEquals("La organización con ID 1 no existe.", exception.getMessage());
        verify(volunteerRepository, times(1)).existsById(1);
        verify(organizationRepository, times(1)).existsById(1);
    }


    @Test
    void testGetVolunteerOrganizationDetails_OrganizationNotFound() {
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerOrganizationService.getVolunteerOrganizationDetails(1);
        });

        assertEquals("VolunteerOrganization not found", exception.getMessage());
    }

    @Test
    void testGetVolunteerOrganizationDetails_PostulationNotFound() {
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganization));
        when(postulationRepository.findById(1)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerOrganizationService.getVolunteerOrganizationDetails(1);
        });

        assertEquals("Postulation not found", exception.getMessage());
    }

    @Test
    void testGetVolunteerOrganizationDetails_DataShareNotFound() {
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganization));
        when(postulationRepository.findById(1)).thenReturn(Optional.of(new PostulationEntity()));
        when(dataShareRepository.findById(1)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerOrganizationService.getVolunteerOrganizationDetails(1);
        });

        assertEquals("DataShare not found", exception.getMessage());
    }

    @Test
    void testGetVolunteersByOrganizationIdAndStatus_EmptyList() {
        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(Collections.emptyList());

        List<StatusVolunteerOrganizationResponse> result = volunteerOrganizationService.getVolunteersByOrganizationIdAndStatus(1, OrganizationStatusEnum.PENDIENTE);

        assertTrue(result.isEmpty());
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
    }

    @Test
    void testGetRecentOrganizationsByVolunteerId_NoVolunteerOrganizations() {
        Integer volunteerId = 1;
        when(volunteerOrganizationRepository.findByVolunteerId(volunteerId))
                .thenReturn(Collections.emptyList());
        List<OrganizationEntity> recentOrganizations = volunteerOrganizationService.getRecentOrganizationsByVolunteerId(volunteerId);
        assertTrue(recentOrganizations.isEmpty());
    }

    @Test
    void testGetRecentOrganizationsByVolunteerId_NoRecentPostulations() {
        Integer volunteerId = 1;
        volunteerOrganization.setId(101);
        when(volunteerOrganizationRepository.findByVolunteerId(volunteerId))
                .thenReturn(Collections.singletonList(volunteerOrganization));
        when(postulationRepository.findByVolunteerOrganizationIdIn(Collections.singletonList(101)))
                .thenReturn(Collections.emptyList());
        List<OrganizationEntity> recentOrganizations = volunteerOrganizationService.getRecentOrganizationsByVolunteerId(volunteerId);
        assertTrue(recentOrganizations.isEmpty());
    }

    @Test
    void testFindAcceptedVolunteerOrganizationsByOrganizationId() {
        when(volunteerOrganizationRepository.findByOrganizationId(1))
                .thenReturn(List.of(volunteerOrganization));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.ACEPTADO, List.of(1)))
                .thenReturn(List.of(postulation));
        when(volunteerOrganizationRepository.findById(1))
                .thenReturn(Optional.of(volunteerOrganization));

        List<VolunteerOrganizationEntity> result = volunteerOrganizationService.findAcceptedVolunteerOrganizationsByOrganizationId(1);

        assertEquals(1, result.size());
        assertEquals(volunteerOrganization, result.get(0));
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
    }

    @Test
    void testCountAcceptedVolunteerOrganizationsByOrganizationId() {
        when(volunteerOrganizationRepository.findByOrganizationId(1))
                .thenReturn(List.of(volunteerOrganization));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.ACEPTADO, List.of(1)))
                .thenReturn(List.of(postulation));

        long count = volunteerOrganizationService.countAcceptedVolunteerOrganizationsByOrganizationId(1);

        assertEquals(1, count);
        verify(postulationRepository, times(1)).findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.ACEPTADO, List.of(1));
    }

    @Test
    void testFindFiveVolunteer() {
        VolunteerEntity volunteer = new VolunteerEntity();
        volunteer.setId(1);
        volunteer.setUserId(100);

        when(volunteerOrganizationRepository.findByOrganizationId(1))
                .thenReturn(List.of(volunteerOrganization));
        when(postulationRepository.findByStatusAndVolunteerOrganizationIdIn(OrganizationStatusEnum.ACEPTADO, List.of(1)))
                .thenReturn(List.of(postulation));
        when(volunteerOrganizationRepository.findById(1))
                .thenReturn(Optional.of(volunteerOrganization));
        when(volunteerRepository.findById(1))
                .thenReturn(Optional.of(volunteer));

        List<VolunteerEntity> result = volunteerOrganizationService.findFiveVolunteer(1);

        assertEquals(1, result.size());
        assertEquals(volunteer.getId(), result.get(0).getId());
        verify(volunteerRepository, times(1)).findById(1);
    }

    @Test
    void testGetRecentOrganizationsByVolunteerId() {
        VolunteerOrganizationEntity volunteerOrganization1 = new VolunteerOrganizationEntity();
        volunteerOrganization1.setId(1);
        volunteerOrganization1.setOrganizationId(100);
        VolunteerOrganizationEntity volunteerOrganization2 = new VolunteerOrganizationEntity();
        volunteerOrganization2.setId(2);
        volunteerOrganization2.setOrganizationId(101);

        PostulationEntity postulation1 = new PostulationEntity();
        postulation1.setVolunteerOrganizationId(1);
        postulation1.setRegistrationDate(LocalDate.of(2024, 10, 20));
        postulation1.setStatus(OrganizationStatusEnum.ACEPTADO);

        PostulationEntity postulation2 = new PostulationEntity();
        postulation2.setVolunteerOrganizationId(2);
        postulation2.setRegistrationDate(LocalDate.of(2024, 10, 18));
        postulation2.setStatus(OrganizationStatusEnum.ACEPTADO);

        OrganizationEntity organization1 = new OrganizationEntity();
        organization1.setId(100);
        OrganizationEntity organization2 = new OrganizationEntity();
        organization2.setId(101);

        when(volunteerOrganizationRepository.findByVolunteerId(1))
                .thenReturn(Arrays.asList(volunteerOrganization1, volunteerOrganization2));
        when(postulationRepository.findByVolunteerOrganizationIdIn(List.of(1, 2)))
                .thenReturn(Arrays.asList(postulation1, postulation2));
        when(volunteerOrganizationRepository.findById(1))
                .thenReturn(Optional.of(volunteerOrganization1));
        when(volunteerOrganizationRepository.findById(2))
                .thenReturn(Optional.of(volunteerOrganization2));
        when(organizationRepository.findAllById(List.of(100, 101)))
                .thenReturn(List.of(organization1, organization2));

        List<OrganizationEntity> recentOrganizations = volunteerOrganizationService.getRecentOrganizationsByVolunteerId(1);

        // Verificar que las organizaciones se obtuvieron correctamente
        assertEquals(2, recentOrganizations.size());
        assertEquals(organization1.getId(), recentOrganizations.get(0).getId());
        assertEquals(organization2.getId(), recentOrganizations.get(1).getId());

        verify(organizationRepository, times(1)).findAllById(List.of(100, 101));
    }
}
