package app.pi_fisio.service;


import app.pi_fisio.dto.ExerciseAuditDTO;
import app.pi_fisio.dto.PageDTO;
import app.pi_fisio.entity.AuditRevisionEntity;
import app.pi_fisio.entity.Exercise;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseAuditService {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public PageDTO<ExerciseAuditDTO> getExerciseRevisions(Long exerciseId, int page, int size) {
        log.info("Buscando revisões para o exercício ID: {}", exerciseId);

        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        Pageable pageable = PageRequest.of(page, size);

        List<Object[]> revisionsRaw = getRawRevisions(auditReader, pageable, exerciseId);

        long totalRevisions = getTotalRevisions(auditReader, exerciseId);

        List<ExerciseAuditDTO> revisionsDTO = revisionsRaw.stream()
                .map(row -> {
                    Exercise exercise = (Exercise) row[0];
                    AuditRevisionEntity revisionEntity = (AuditRevisionEntity) row[1];
                    RevisionType revisionType = (RevisionType) row[2];

                    LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(revisionEntity.getTimestamp()), ZoneId.systemDefault());

                    return new ExerciseAuditDTO(
                            (long) revisionEntity.getId(),
                            timestamp,
                            revisionEntity.getUsername(),
                            revisionType,
                            exercise
                    );
                })
                .toList();

        log.info("Encontradas {} revisões para o exercício ID {}. Total de revisões: {}", revisionsDTO.size(), exerciseId, totalRevisions);
        int totalPages = (int) Math.ceil((double) totalRevisions / size);
        if (totalRevisions == 0) { // Se não houver revisões, o totalPages seja 0 e não 1
            totalPages = 0;
        }
        return new PageDTO<>(revisionsDTO, totalRevisions, totalPages);

    }

    private List<Object[]> getRawRevisions(AuditReader reader, Pageable pageable, Long exerciseId) {
        if (exerciseId != null) {
            return reader.createQuery()
                    .forRevisionsOfEntity(Exercise.class, false, true)
                    .add(AuditEntity.id().eq(exerciseId))
                    .addOrder(AuditEntity.revisionNumber().desc())
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        } else {
            return reader.createQuery()
                    .forRevisionsOfEntity(Exercise.class, false, true)
                    .addOrder(AuditEntity.revisionNumber().desc())
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
    }

    private Long getTotalRevisions(AuditReader reader, Long exerciseId) {
        Number revisionCount;
        if (exerciseId != null) {
            revisionCount = (Number) reader.createQuery()
                    .forRevisionsOfEntity(Exercise.class, false, true)
                    .add(AuditEntity.id().eq(exerciseId))
                    .addProjection(AuditEntity.revisionNumber().count())
                    .getSingleResult();
        } else {
            revisionCount = (Number) reader.createQuery()
                    .forRevisionsOfEntity(Exercise.class, false, true)
                    .addProjection(AuditEntity.revisionNumber().count())
                    .getSingleResult();
        }
        return revisionCount.longValue();
    }


    @Transactional(readOnly = true)
    public List<ExerciseAuditDTO> test() {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditQuery query = reader.createQuery()
                .forRevisionsOfEntity(Exercise.class, false, true);
        query.addOrder(AuditEntity.revisionNumber().desc());

        List<Object[]> rawResults = query.getResultList();

        List<ExerciseAuditDTO> result = rawResults.stream()
                .map(row -> {
                    Exercise exercise = (Exercise) row[0];
                    AuditRevisionEntity revisionEntity = (AuditRevisionEntity) row[1];
                    RevisionType revisionType = (RevisionType) row[2];

                    LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(revisionEntity.getTimestamp()), ZoneId.systemDefault());

                    return new ExerciseAuditDTO(
                            (long) revisionEntity.getId(),
                            timestamp,
                            revisionEntity.getUsername(),
                            revisionType,
                            exercise
                    );
                })
                .collect(Collectors.toList());

        return result;
    }
}