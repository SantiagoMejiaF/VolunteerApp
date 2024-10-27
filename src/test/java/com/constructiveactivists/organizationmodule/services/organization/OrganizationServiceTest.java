package com.constructiveactivists.organizationmodule.services.organization;

import com.constructiveactivists.configurationmodule.exceptions.BusinessException;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.entities.organization.enums.OrganizationTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.SectorTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.VolunteeringTypeEnum;
import com.constructiveactivists.organizationmodule.repositories.OrganizationRepository;
import com.constructiveactivists.usermodule.entities.UserEntity;
import com.constructiveactivists.usermodule.entities.RoleEntity;
import com.constructiveactivists.usermodule.entities.enums.RoleType;
import com.constructiveactivists.usermodule.services.UserService;
import com.constructiveactivists.volunteermodule.services.volunteerorganization.PostulationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostulationService postulationService;

    @InjectMocks
    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveOrganization_Success() {
        Integer userId = 1;
        OrganizationEntity organization = new OrganizationEntity();
        organization.setUserId(userId);

        UserEntity user = new UserEntity();
        user.setId(userId);

        RoleEntity role = new RoleEntity();
        role.setRoleType(RoleType.SIN_ASIGNAR);
        user.setRole(role);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(organizationRepository.save(any(OrganizationEntity.class))).thenReturn(organization);

        OrganizationEntity savedOrganization = organizationService.saveOrganization(organization);

        assertEquals(userId, savedOrganization.getUserId());
        verify(userService, times(1)).updateUserRoleType(userId, RoleType.ORGANIZACION); // Verifica la actualización del rol
        verify(organizationRepository, times(1)).save(organization);
    }

    @Test
    void testSaveOrganization_UserAlreadyHasRole() {
        Integer userId = 1;
        OrganizationEntity organization = new OrganizationEntity();
        organization.setUserId(userId);

        UserEntity user = new UserEntity();
        user.setId(userId);

        RoleEntity role = new RoleEntity();
        role.setRoleType(RoleType.ORGANIZACION);
        user.setRole(role);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        assertThrows(BusinessException.class, () -> organizationService.saveOrganization(organization));
        verify(organizationRepository, times(0)).save(any(OrganizationEntity.class)); // No se debe guardar si ya tiene rol
    }

    @Test
    void testSaveOrganization_UserNotFound() {
        Integer userId = 1;
        OrganizationEntity organization = new OrganizationEntity();
        organization.setUserId(userId);

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> organizationService.saveOrganization(organization));
        verify(organizationRepository, times(0)).save(any(OrganizationEntity.class));
    }

    @Test
    void testUpdateOrganization_Success() {
        Integer organizationId = 1;
        OrganizationEntity existingOrganization = new OrganizationEntity();
        existingOrganization.setId(organizationId);
        existingOrganization.setOrganizationName("Old Name");

        OrganizationEntity updatedOrganization = new OrganizationEntity();
        updatedOrganization.setOrganizationName("New Name");

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(existingOrganization));
        when(organizationRepository.save(any(OrganizationEntity.class))).thenReturn(existingOrganization);

        OrganizationEntity result = organizationService.updateOrganization(organizationId, updatedOrganization);

        assertEquals("New Name", result.getOrganizationName());
        verify(organizationRepository, times(1)).save(existingOrganization);
    }

    @Test
    void testUpdateOrganization_NotFound() {
        Integer organizationId = 1;
        OrganizationEntity updatedOrganization = new OrganizationEntity();
        updatedOrganization.setOrganizationName("New Name");

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> organizationService.updateOrganization(organizationId, updatedOrganization));
        verify(organizationRepository, times(0)).save(any(OrganizationEntity.class));
    }

    @Test
    void testDeleteOrganization_Success() {
        Integer organizationId = 1;
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(organizationId);
        organization.setUserId(2);

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));

        organizationService.deleteOrganization(organizationId);

        verify(userService, times(1)).deleteUser(2);
        verify(organizationRepository, times(1)).delete(organization);
    }

    @Test
    void testDeleteOrganization_NotFound() {
        Integer organizationId = 1;

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> organizationService.deleteOrganization(organizationId));
        verify(userService, times(0)).deleteUser(anyInt());
        verify(organizationRepository, times(0)).delete(any(OrganizationEntity.class));
    }

    @Test
    void testGetAllOrganizations() {
        List<OrganizationEntity> organizations = List.of(new OrganizationEntity(), new OrganizationEntity());

        when(organizationRepository.findAll()).thenReturn(organizations);

        List<OrganizationEntity> result = organizationService.getAllOrganizations();

        assertEquals(2, result.size());
        verify(organizationRepository, times(1)).findAll();
    }

    @Test
    void testGetOrganizationById_Found() {
        Integer organizationId = 1;
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(organizationId);

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));

        Optional<OrganizationEntity> result = organizationService.getOrganizationById(organizationId);

        assertTrue(result.isPresent());
        assertEquals(organizationId, result.get().getId());
        verify(organizationRepository, times(1)).findById(organizationId);
    }

    @Test
    void testGetOrganizationById_NotFound() {
        Integer organizationId = 1;

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

        Optional<OrganizationEntity> result = organizationService.getOrganizationById(organizationId);

        assertFalse(result.isPresent());
        verify(organizationRepository, times(1)).findById(organizationId);
    }

    @Test
    void testGetOrganizationByUserId_Found() {
        Integer userId = 1;
        OrganizationEntity organization = new OrganizationEntity();
        organization.setUserId(userId);

        when(organizationRepository.findByUserId(userId)).thenReturn(Optional.of(organization));

        Optional<OrganizationEntity> result = organizationService.getOrganizationByUserId(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
        verify(organizationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetOrganizationByUserId_NotFound() {
        Integer userId = 1;

        when(organizationRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Optional<OrganizationEntity> result = organizationService.getOrganizationByUserId(userId);

        assertFalse(result.isPresent());
        verify(organizationRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetAllSectors() {
        List<SectorTypeEnum> expectedSectors = List.of(
                SectorTypeEnum.TECNOLOGIA,
                SectorTypeEnum.SALUD,
                SectorTypeEnum.EDUCACION,
                SectorTypeEnum.FINANZAS,
                SectorTypeEnum.MANUFACTURA,
                SectorTypeEnum.SERVICIOS,
                SectorTypeEnum.GOBIERNO,
                SectorTypeEnum.AGRICULTURA,
                SectorTypeEnum.TRANSPORTE,
                SectorTypeEnum.ENERGIA,
                SectorTypeEnum.OTRO
        );

        List<SectorTypeEnum> result = organizationService.getAllSectors();

        assertEquals(expectedSectors.size(), result.size());
        assertTrue(result.containsAll(expectedSectors));
    }

    @Test
    void testGetAllVolunteeringTypes() {
        List<VolunteeringTypeEnum> expectedVolunteeringTypes = List.of(
                VolunteeringTypeEnum.ADJUNTA,
                VolunteeringTypeEnum.DONANTE,
                VolunteeringTypeEnum.PATROCINADORA,
                VolunteeringTypeEnum.OTRO
        );

        List<VolunteeringTypeEnum> result = organizationService.getAllVolunteeringTypes();

        assertEquals(expectedVolunteeringTypes.size(), result.size());
        assertTrue(result.containsAll(expectedVolunteeringTypes));
    }

    @Test
    void testApproveVolunteer() {
        Integer volunteerOrganizationId = 1;

        doNothing().when(postulationService).updateStatusAccept(volunteerOrganizationId);

        organizationService.approveVolunteer(volunteerOrganizationId);

        verify(postulationService, times(1)).updateStatusAccept(volunteerOrganizationId);
    }

    @Test
    void testRejectVolunteer() {
        Integer volunteerOrganizationId = 1;

        doNothing().when(postulationService).updateStatusRefuse(volunteerOrganizationId);

        organizationService.rejectVolunteer(volunteerOrganizationId);

        verify(postulationService, times(1)).updateStatusRefuse(volunteerOrganizationId);
    }

    @Test
    void testGetAllOrganizationTypes() {
        List<OrganizationTypeEnum> expectedOrganizationTypes = List.of(
                OrganizationTypeEnum.EMPRESA,
                OrganizationTypeEnum.ONG,
                OrganizationTypeEnum.GOBIERNO,
                OrganizationTypeEnum.INSTITUCION_EDUCATIVA,
                OrganizationTypeEnum.COOPERATIVA,
                OrganizationTypeEnum.FUNDACION,
                OrganizationTypeEnum.ASOCIACION,
                OrganizationTypeEnum.SINDICATO,
                OrganizationTypeEnum.STARTUP,
                OrganizationTypeEnum.CORPORACION,
                OrganizationTypeEnum.OTRO
        );

        List<OrganizationTypeEnum> result = organizationService.getAllOrganizationTypes();

        assertEquals(expectedOrganizationTypes.size(), result.size());
        assertTrue(result.containsAll(expectedOrganizationTypes));
    }
}
