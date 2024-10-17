package com.constructiveactivists.organizationmodule.controllers;

import com.constructiveactivists.organizationmodule.controllers.request.organization.OrganizationRequest;
import com.constructiveactivists.organizationmodule.controllers.request.organization.OrganizationUpdateRequest;
import com.constructiveactivists.organizationmodule.entities.organization.OrganizationEntity;
import com.constructiveactivists.organizationmodule.entities.organization.enums.OrganizationTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.SectorTypeEnum;
import com.constructiveactivists.organizationmodule.entities.organization.enums.VolunteeringTypeEnum;
import com.constructiveactivists.organizationmodule.mappers.organization.OrganizationMapper;
import com.constructiveactivists.organizationmodule.mappers.organization.OrganizationUpdateMapper;
import com.constructiveactivists.organizationmodule.services.organization.OrganizationService;
import com.constructiveactivists.organizationmodule.services.organization.VolunteerApprovalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrganizationControllerTest {

    @Mock
    private OrganizationService organizationService;

    @Mock
    private OrganizationMapper organizationMapper;

    @Mock
    private OrganizationUpdateMapper organizationUpdateMapper;

    @Mock
    private VolunteerApprovalService volunteerApprovalService;

    @InjectMocks
    private OrganizationController organizationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOrganizationById_Success() {
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(1);
        when(organizationService.getOrganizationById(1)).thenReturn(Optional.of(organization));

        ResponseEntity<OrganizationEntity> response = organizationController.getOrganizationById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(organization, response.getBody());
        verify(organizationService, times(1)).getOrganizationById(1);
    }

    @Test
    void testGetOrganizationById_NotFound() {
        when(organizationService.getOrganizationById(1)).thenReturn(Optional.empty());

        ResponseEntity<OrganizationEntity> response = organizationController.getOrganizationById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(organizationService, times(1)).getOrganizationById(1);
    }

    @Test
    void testCreateOrganization_Success() {
        OrganizationRequest request = new OrganizationRequest();
        OrganizationEntity entity = new OrganizationEntity();
        entity.setId(1);

        when(organizationMapper.toDomain(any(OrganizationRequest.class))).thenReturn(entity);
        when(organizationService.saveOrganization(any(OrganizationEntity.class))).thenReturn(entity);

        ResponseEntity<OrganizationEntity> response = organizationController.createOrganization(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(entity, response.getBody());
        verify(organizationService, times(1)).saveOrganization(any(OrganizationEntity.class));
    }

    @Test
    void testUpdateOrganization_Success() {
        OrganizationUpdateRequest updateRequest = new OrganizationUpdateRequest();
        OrganizationEntity updatedEntity = new OrganizationEntity();
        updatedEntity.setId(1);
        updatedEntity.setOrganizationName("Updated Organization");

        when(organizationUpdateMapper.toDomain(any(OrganizationUpdateRequest.class))).thenReturn(updatedEntity);
        when(organizationService.updateOrganization(eq(1), any(OrganizationEntity.class))).thenReturn(updatedEntity);

        ResponseEntity<OrganizationEntity> response = organizationController.updateOrganization(1, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedEntity, response.getBody());
        verify(organizationService, times(1)).updateOrganization(eq(1), any(OrganizationEntity.class));
    }

    @Test
    void testDeleteOrganization_Success() {
        doNothing().when(organizationService).deleteOrganization(1);

        ResponseEntity<Void> response = organizationController.deleteOrganization(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(organizationService, times(1)).deleteOrganization(1);
    }

    @Test
    void testSendVolunteerApproval_Success() {
        doNothing().when(volunteerApprovalService).sendVolunteerApprovalResponse(1, 1, true);

        ResponseEntity<Void> response = organizationController.sendVolunteerApprovalResponse(1, 1, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(volunteerApprovalService, times(1)).sendVolunteerApprovalResponse(1, 1, true);
    }

    @Test
    void testGetAllVolunteeringTypes_Success() {
        List<VolunteeringTypeEnum> volunteeringTypes = Arrays.asList(VolunteeringTypeEnum.ADJUNTA, VolunteeringTypeEnum.DONANTE);
        when(organizationService.getAllVolunteeringTypes()).thenReturn(volunteeringTypes);

        ResponseEntity<List<VolunteeringTypeEnum>> response = organizationController.getAllVolunteeringTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(volunteeringTypes, response.getBody());
        verify(organizationService, times(1)).getAllVolunteeringTypes();
    }

    @Test
    void testGetAllVolunteeringTypes_NotFound() {
        when(organizationService.getAllVolunteeringTypes()).thenReturn(List.of());

        ResponseEntity<List<VolunteeringTypeEnum>> response = organizationController.getAllVolunteeringTypes();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(organizationService, times(1)).getAllVolunteeringTypes();
    }

    @Test
    void testGetAllOrganizationTypes_Success() {
        List<OrganizationTypeEnum> organizationTypes = Arrays.asList(OrganizationTypeEnum.EMPRESA, OrganizationTypeEnum.ONG);
        when(organizationService.getAllOrganizationTypes()).thenReturn(organizationTypes);

        ResponseEntity<List<OrganizationTypeEnum>> response = organizationController.getAllOrganizationTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(organizationTypes, response.getBody());
        verify(organizationService, times(1)).getAllOrganizationTypes();
    }

    @Test
    void testGetAllOrganizationTypes_NotFound() {
        when(organizationService.getAllOrganizationTypes()).thenReturn(List.of());

        ResponseEntity<List<OrganizationTypeEnum>> response = organizationController.getAllOrganizationTypes();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(organizationService, times(1)).getAllOrganizationTypes();
    }

    @Test
    void testGetAllSectors_Success() {
        List<SectorTypeEnum> sectorTypes = Arrays.asList(SectorTypeEnum.TECNOLOGIA, SectorTypeEnum.SALUD);
        when(organizationService.getAllSectors()).thenReturn(sectorTypes);

        ResponseEntity<List<SectorTypeEnum>> response = organizationController.getAllSectors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sectorTypes, response.getBody());
        verify(organizationService, times(1)).getAllSectors();
    }

    @Test
    void testGetAllSectors_NotFound() {
        when(organizationService.getAllSectors()).thenReturn(List.of());

        ResponseEntity<List<SectorTypeEnum>> response = organizationController.getAllSectors();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(organizationService, times(1)).getAllSectors();
    }

    @Test
    void testGetOrganizationByUserId_Success() {
        OrganizationEntity organization = new OrganizationEntity();
        organization.setId(1);
        when(organizationService.getOrganizationByUserId(1)).thenReturn(Optional.of(organization));

        ResponseEntity<OrganizationEntity> response = organizationController.getOrganizationByUserId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(organization, response.getBody());
        verify(organizationService, times(1)).getOrganizationByUserId(1);
    }

    @Test
    void testGetOrganizationByUserId_NotFound() {
        when(organizationService.getOrganizationByUserId(1)).thenReturn(Optional.empty());

        ResponseEntity<OrganizationEntity> response = organizationController.getOrganizationByUserId(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(organizationService, times(1)).getOrganizationByUserId(1);
    }

    @Test
    void testGetAllOrganizations_Success() {
        OrganizationEntity org1 = new OrganizationEntity();
        OrganizationEntity org2 = new OrganizationEntity();

        when(organizationService.getAllOrganizations()).thenReturn(Arrays.asList(org1, org2));

        ResponseEntity<List<OrganizationEntity>> response = organizationController.getAllOrganizations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(organizationService, times(1)).getAllOrganizations();
    }

    @Test
    void testGetAllOrganizations_NotFound() {
        when(organizationService.getAllOrganizations()).thenReturn(List.of());

        ResponseEntity<List<OrganizationEntity>> response = organizationController.getAllOrganizations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(organizationService, times(1)).getAllOrganizations();
    }
}
