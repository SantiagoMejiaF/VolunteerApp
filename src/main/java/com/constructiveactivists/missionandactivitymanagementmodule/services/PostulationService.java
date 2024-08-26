package com.constructiveactivists.missionandactivitymanagementmodule.services;

import com.constructiveactivists.missionandactivitymanagementmodule.entities.postulation.PostulationEntity;
import com.constructiveactivists.missionandactivitymanagementmodule.entities.postulation.enums.PostulationStatusEnum;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.ActivityRepository;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.MissionRepository;
import com.constructiveactivists.missionandactivitymanagementmodule.repositories.PostulationRepository;
import com.constructiveactivists.volunteermanagementmodule.repositories.VolunteerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostulationService {

    private final PostulationRepository postulationRepository;
    private final VolunteerRepository volunteerRepository;
    private final ActivityRepository activityRepository;
    private final MissionRepository missionRepository;

    public List<PostulationEntity> getAllPostulations() {
        return postulationRepository.findAll();
    }

    public Optional<PostulationEntity> getPostulationById(Integer id) {
        return postulationRepository.findById(id);
    }

    public PostulationEntity updatePostulation(Integer id, PostulationEntity updatedPostulation) {
        PostulationEntity postulation = postulationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La postulación con ID " + id + " no existe."));

        postulation.setStatus(updatedPostulation.getStatus());
        postulation.setComments(updatedPostulation.getComments());
        postulation.setApprovalDate(updatedPostulation.getApprovalDate());
        postulation.setOrganizationId(updatedPostulation.getOrganizationId());

        return postulationRepository.save(postulation);
    }

    public void deletePostulation(Integer id) {
        PostulationEntity postulation = postulationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La postulación con ID " + id + " no existe."));
        postulationRepository.delete(postulation);
    }

    public PostulationEntity approvePostulation(Integer postulationId) {
        PostulationEntity postulation = postulationRepository.findById(postulationId)
                .orElseThrow(() -> new EntityNotFoundException("La postulación con ID " + postulationId + " no existe."));

        postulation.setStatus(PostulationStatusEnum.APROBADA);
        postulation.setApprovalDate(LocalDate.now());

        return postulationRepository.save(postulation);
    }

//    public PostulationEntity createPostulation(PostulationEntity postulation) {
//        // Validar que el voluntario exista
//        VolunteerEntity volunteer = validateVolunteerExists(postulation.getVolunteerId());
//
//        if (postulation.getMission() != null) {
//            validateMissionPostulation(postulation, volunteer);
//        }
//
//        if (postulation.getActivity() != null) {
//            validateActivityPostulation(postulation, volunteer);
//        }
//
//        // Establecer el estado de la postulación y la fecha de aplicación
//        postulation.setStatus(PostulationStatusEnum.PENDIENTE);
//        postulation.setApplicationDate(LocalDate.now());
//
//        // Guardar la postulación en el repositorio
//        return postulationRepository.save(postulation);
//    }
//
//    private VolunteerEntity validateVolunteerExists(Integer volunteerId) {
//        return volunteerRepository.findById(volunteerId)
//                .orElseThrow(() -> new EntityNotFoundException("Voluntario no encontrado."));
//    }
//
//    private void validateMissionPostulation(PostulationEntity postulation, VolunteerEntity volunteer) {
//        MissionEntity mission = missionRepository.findById(postulation.getMission().getId())
//                .orElseThrow(() -> new EntityNotFoundException("Misión no encontrada."));
//
//        validateOrganizationForPostulation(volunteer.getOrganizationId(), mission.getOrganizationId(), "misión");
//    }
//
//    private void validateActivityPostulation(PostulationEntity postulation, VolunteerEntity volunteer) {
//        ActivityEntity activity = activityRepository.findById(postulation.getActivity().getId())
//                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada."));
//
//        MissionEntity mission = activity.getMission();
//
//        validateOrganizationForPostulation(volunteer.getOrganizationId(), mission.getOrganizationId(), "actividad");
//    }
//
//    private void validateOrganizationForPostulation(Integer volunteerOrganizationId, Integer missionOrganizationId, String postulationType) {
//        if (missionOrganizationId == null) {
//            // La misión o actividad es pública
//            if (volunteerOrganizationId != null) {
//                throw new BusinessException("Voluntarios pertenecientes a organizaciones no pueden postularse a " + postulationType + " pública.");
//            }
//        } else {
//            // La misión o actividad es privada
//            if (volunteerOrganizationId == null || !volunteerOrganizationId.equals(missionOrganizationId)) {
//                throw new BusinessException("El ID de la organización del voluntario no coincide con el de la " + postulationType + ".");
//            }
//        }
//    }
}
