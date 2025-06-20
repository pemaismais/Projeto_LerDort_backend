// app.pi_fisio.service.GlobalAuditService.java
package app.pi_fisio.service;

import app.pi_fisio.dto.GlobalAuditDTO;
import app.pi_fisio.dto.PageDTO; // Usando seu PageDTO genérico
import app.pi_fisio.entity.AuditRevisionEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.CrossTypeRevisionChangesReader;
import org.hibernate.envers.RevisionType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GlobalAuditService {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public PageDTO<GlobalAuditDTO> getGlobalRevisions(int page, int size) {
        log.info("Buscando revisões globais para página {} com tamanho {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        CrossTypeRevisionChangesReader crossTypeReader = auditReader.getCrossTypeRevisionChangesReader();

        // 1. Obter e paginar os números de revisão
        List<Number> allRevisionNumbers = findAllRevisionNumbersSortedDesc();
        List<Number> pagedRevisionNumbers = getPagedRevisionNumbers(allRevisionNumbers, pageable);

        // 2. Processar cada revisão paginada para obter os detalhes
        List<GlobalAuditDTO> globalAuditEntries = processPagedRevisions(pagedRevisionNumbers, auditReader, crossTypeReader);

        // 3. Preparar o DTO de paginação
        long totalElements = allRevisionNumbers.size(); // Contagem total de revisões
        int totalPages = calculateTotalPages(totalElements, size);

        log.info("Encontradas {} entradas de auditoria global. Total de revisões: {}", globalAuditEntries.size(), totalElements);

        return new PageDTO<>(globalAuditEntries, totalElements, totalPages);
    }

    private List<Number> findAllRevisionNumbersSortedDesc() {
        return entityManager.createQuery(
                        "SELECT r.id FROM AuditRevisionEntity r ORDER BY r.id DESC", Number.class)
                .getResultList();
    }

    private List<Number> getPagedRevisionNumbers(List<Number> allRevisionNumbers, Pageable pageable) {
        return allRevisionNumbers.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
    }


    private List<GlobalAuditDTO> processPagedRevisions(
            List<Number> pagedRevisionNumbers,
            AuditReader auditReader,
            CrossTypeRevisionChangesReader crossTypeReader
    ) {
        List<GlobalAuditDTO> globalAuditEntries = new ArrayList<>();
        for (Number revisionNumber : pagedRevisionNumbers) {
            AuditRevisionEntity revisionEntity = auditReader.findRevision(AuditRevisionEntity.class, revisionNumber);

            if (revisionEntity == null) {
                log.warn("Revision entity not found for revision number: {}", revisionNumber);
                continue;
            }

            globalAuditEntries.addAll(mapRevisionToAuditDTOs(revisionEntity, crossTypeReader));
        }

        // Ordena os resultados finais por timestamp e depois por ID de revisão para consistência
        globalAuditEntries.sort(Comparator
                .comparing(GlobalAuditDTO::revisionTimestamp).reversed()
                .thenComparing(GlobalAuditDTO::revisionId).reversed()
        );
        return globalAuditEntries;
    }


    private List<GlobalAuditDTO> mapRevisionToAuditDTOs(
            AuditRevisionEntity revisionEntity,
            CrossTypeRevisionChangesReader crossTypeReader
    ) {
        List<GlobalAuditDTO> revisionAuditDTOs = new ArrayList<>();
        LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(revisionEntity.getTimestamp()), ZoneId.systemDefault());

        Map<RevisionType, List<Object>> changes = crossTypeReader.findEntitiesGroupByRevisionType(revisionEntity.getId());

        changes.forEach((revisionType, entities) -> {
            entities.forEach(entity -> {
                String entityName = entity.getClass().getSimpleName();
                revisionAuditDTOs.add(new GlobalAuditDTO(
                        (long) revisionEntity.getId(),
                        timestamp,
                        revisionEntity.getUsername(),
                        revisionType,
                        entityName,
                        entity
                ));
            });
        });
        return revisionAuditDTOs;
    }

    private int calculateTotalPages(long totalElements, int pageSize) {
        if (totalElements == 0 || pageSize == 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElements / pageSize);
    }
}