package com.constructiveactivists.volunteermodule.services.volunteerorganization;

import com.constructiveactivists.volunteermodule.entities.volunteerorganization.DataShareVolunteerOrganizationEntity;
import com.constructiveactivists.volunteermodule.repositories.DataShareVolunteerOrganizationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataShareVolunteerOrganizationServiceTest {

    @Mock
    private DataShareVolunteerOrganizationRepository dataShareVolunteerOrganizationRepository;

    @InjectMocks
    private DataShareVolunteerOrganizationService dataShareVolunteerOrganizationService;

    private DataShareVolunteerOrganizationEntity dataShareEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataShareEntity = new DataShareVolunteerOrganizationEntity();
        dataShareEntity.setVolunteerOrganizationId(1);
        dataShareEntity.setHoursDone(100);
        dataShareEntity.setHoursCertified(50);
        dataShareEntity.setMonthlyHours(20);
    }

    @Test
    void testUpdateHours_Success() {
        when(dataShareVolunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(dataShareEntity));
        when(dataShareVolunteerOrganizationRepository.save(any(DataShareVolunteerOrganizationEntity.class))).thenReturn(dataShareEntity);

        DataShareVolunteerOrganizationEntity result = dataShareVolunteerOrganizationService.updateHours(1, 120, 60, 30);

        assertNotNull(result);
        assertEquals(120, result.getHoursDone());
        assertEquals(60, result.getHoursCertified());
        assertEquals(30, result.getMonthlyHours());
        verify(dataShareVolunteerOrganizationRepository, times(1)).findById(1);
        verify(dataShareVolunteerOrganizationRepository, times(1)).save(dataShareEntity);
    }

    @Test
    void testUpdateHours_EntityNotFound() {
        when(dataShareVolunteerOrganizationRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            dataShareVolunteerOrganizationService.updateHours(1, 120, 60, 30);
        });

        assertEquals("Registro no encontrado con ID: 1", exception.getMessage());
        verify(dataShareVolunteerOrganizationRepository, times(1)).findById(1);
        verify(dataShareVolunteerOrganizationRepository, never()).save(any(DataShareVolunteerOrganizationEntity.class));
    }

    @Test
    void testAddDataShareVolunteerOrganization_Success() {
        dataShareVolunteerOrganizationService.addDataShareVolunteerOrganization(2);

        verify(dataShareVolunteerOrganizationRepository, times(1)).save(any(DataShareVolunteerOrganizationEntity.class));
    }

    @Test
    void testFindById_Success() {
        when(dataShareVolunteerOrganizationRepository.findById(1)).thenReturn(Optional.of(dataShareEntity));

        DataShareVolunteerOrganizationEntity result = dataShareVolunteerOrganizationService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getVolunteerOrganizationId());
        verify(dataShareVolunteerOrganizationRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_EntityNotFound() {
        when(dataShareVolunteerOrganizationRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            dataShareVolunteerOrganizationService.findById(1);
        });

        assertEquals("Registro no encontrado con ID: 1", exception.getMessage());
        verify(dataShareVolunteerOrganizationRepository, times(1)).findById(1);
    }

    @Test
    void testFindAll_Success() {
        List<DataShareVolunteerOrganizationEntity> entities = Arrays.asList(dataShareEntity, new DataShareVolunteerOrganizationEntity());
        when(dataShareVolunteerOrganizationRepository.findAll()).thenReturn(entities);

        List<DataShareVolunteerOrganizationEntity> result = dataShareVolunteerOrganizationService.findAll();

        assertEquals(2, result.size());
        verify(dataShareVolunteerOrganizationRepository, times(1)).findAll();
    }

    @Test
    void testFindAllByVolunteerOrganizationIdIn_Success() {
        List<Integer> ids = Arrays.asList(1, 2, 3);
        List<DataShareVolunteerOrganizationEntity> entities = Arrays.asList(dataShareEntity, new DataShareVolunteerOrganizationEntity());
        when(dataShareVolunteerOrganizationRepository.findAllByVolunteerOrganizationIdIn(ids)).thenReturn(entities);

        List<DataShareVolunteerOrganizationEntity> result = dataShareVolunteerOrganizationService.findAllByVolunteerOrganizationIdIn(ids);

        assertEquals(2, result.size());
        verify(dataShareVolunteerOrganizationRepository, times(1)).findAllByVolunteerOrganizationIdIn(ids);
    }
}
