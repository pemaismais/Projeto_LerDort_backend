package app.pi_fisio.dto;

import java.time.LocalDateTime;

import app.pi_fisio.entity.Exercise;
import org.hibernate.envers.RevisionType;

public record ExerciseAuditDTO(
        Long revisionNumber,
        LocalDateTime revisionTimestamp,
        String username,
        RevisionType revisionType,
        Exercise exercise
) {}