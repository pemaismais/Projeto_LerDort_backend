package app.pi_fisio.dto;

import app.pi_fisio.entity.Intensity;
import app.pi_fisio.entity.Joint;

import java.util.List;
import java.util.Set;

public record ExerciseAuditPageDTO(List<ExerciseAuditDTO> exerciseAudits, Long totalElements, int totalPages) {
}