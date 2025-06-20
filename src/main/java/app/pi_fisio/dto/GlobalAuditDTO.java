package app.pi_fisio.dto;

import org.hibernate.envers.RevisionType;

import java.time.LocalDateTime;


public record GlobalAuditDTO(
        Long revisionId,
        LocalDateTime revisionTimestamp,
        String username,
        RevisionType revisionType,
        String entityName, // Nome da classe da entidade
        Object entity // A entidade em si
) {}