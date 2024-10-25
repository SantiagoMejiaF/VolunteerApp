package com.constructiveactivists.volunteermodule.services.volunteerorganization;

import com.constructiveactivists.missionandactivitymodule.repositories.configurationmodule.exceptions.BusinessException;
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
import java.time.LocalDateTime;
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

    private VolunteerOrganizationEntity volunteerOrganizationEntity;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        volunteerOrganizationEntity = new VolunteerOrganizationEntity();
        volunteerOrganizationEntity.setId(1);
        volunteerOrganizationEntity.setVolunteerId(1);
        volunteerOrganizationEntity.setOrganizationId(1);
    }

    @Test
    void testSaveVolunteerOrganization_Success() {
        when(volunteerRepository.existsById(1)).thenReturn(true);
        when(organizationRepository.existsById(1)).thenReturn(true);
        when(volunteerOrganizationRepository.save(any(VolunteerOrganizationEntity.class))).thenReturn(volunteerOrganizationEntity);

        VolunteerOrganizationEntity result = volunteerOrganizationService.save(volunteerOrganizationEntity);

        assertNotNull(result);
        assertEquals(1, result.getVolunteerId());
        verify(volunteerRepository, times(1)).existsById(1);
        verify(organizationRepository, times(1)).existsById(1);
        verify(volunteerOrganizationRepository, times(1)).save(volunteerOrganizationEntity);
    }

    @Test
    void testGetVolunteerOrganizationDetails_Success() {
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganizationEntity));
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
        volunteerOrganizationEntity.setId(1);
        volunteerOrganizationEntity.setVolunteerId(1);

        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganizationEntity);

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
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganizationEntity));
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
        volunteerOrganizationEntity.setVolunteerId(1);

        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganizationEntity);

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
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganizationEntity));
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
        volunteerOrganizationEntity.setId(1);
        volunteerOrganizationEntity.setVolunteerId(1);

        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganizationEntity);

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
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganizationEntity));
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
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
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
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
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
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
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
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();

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
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        when(volunteerOrganizationRepository.findByVolunteerId(1)).thenReturn(volunteerOrganizations);

        List<VolunteerOrganizationEntity> result = volunteerOrganizationService.getOrganizationsByVolunteerId(1);

        assertEquals(1, result.size());
        verify(volunteerOrganizationRepository, times(1)).findByVolunteerId(1);
    }

    @Test
    void testGetVolunteersByOrganizationId() {
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        when(volunteerOrganizationRepository.findByOrganizationId(1)).thenReturn(volunteerOrganizations);

        List<VolunteerOrganizationEntity> result = volunteerOrganizationService.getVolunteersByOrganizationId(1);

        assertEquals(1, result.size());
        verify(volunteerOrganizationRepository, times(1)).findByOrganizationId(1);
    }

    @Test
    void testFindAll() {
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        List<VolunteerOrganizationEntity> volunteerOrganizations = List.of(volunteerOrganization);

        when(volunteerOrganizationRepository.findAll()).thenReturn(volunteerOrganizations);

        List<VolunteerOrganizationEntity> result = volunteerOrganizationService.findAll();

        assertEquals(1, result.size());
        verify(volunteerOrganizationRepository, times(1)).findAll();
    }

    @Test
    void testFindVolunteerOrganizationIdsByOrganizationId() {
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
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
            volunteerOrganizationService.save(volunteerOrganizationEntity);
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
            volunteerOrganizationService.save(volunteerOrganizationEntity);
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
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganizationEntity));
        when(postulationRepository.findById(1)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            volunteerOrganizationService.getVolunteerOrganizationDetails(1);
        });

        assertEquals("Postulation not found", exception.getMessage());
    }

    @Test
    void testGetVolunteerOrganizationDetails_DataShareNotFound() {
        when(volunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(volunteerOrganizationEntity));
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
    public void testGetRecentOrganizationsByVolunteerId_NoVolunteerOrganizations() {
        Integer volunteerId = 1;
        when(volunteerOrganizationRepository.findByVolunteerId(volunteerId))
                .thenReturn(Collections.emptyList());
        List<OrganizationEntity> recentOrganizations = volunteerOrganizationService.getRecentOrganizationsByVolunteerId(volunteerId);
        assertTrue(recentOrganizations.isEmpty());
    }

    @Test
    public void testGetRecentOrganizationsByVolunteerId_NoRecentPostulations() {
        Integer volunteerId = 1;
        VolunteerOrganizationEntity volunteerOrganization = new VolunteerOrganizationEntity();
        volunteerOrganization.setId(101);
        when(volunteerOrganizationRepository.findByVolunteerId(volunteerId))
                .thenReturn(Arrays.asList(volunteerOrganization));
        when(postulationRepository.findByVolunteerOrganizationIdIn(Collections.singletonList(101)))
                .thenReturn(Collections.emptyList());
        List<OrganizationEntity> recentOrganizations = volunteerOrganizationService.getRecentOrganizationsByVolunteerId(volunteerId);
        assertTrue(recentOrganizations.isEmpty());
    }



}
