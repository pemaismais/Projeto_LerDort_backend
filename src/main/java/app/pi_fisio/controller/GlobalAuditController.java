package app.pi_fisio.controller;

import app.pi_fisio.dto.GlobalAuditDTO;
import app.pi_fisio.dto.PageDTO;
import app.pi_fisio.service.GlobalAuditService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Slf4j
public class GlobalAuditController {

    private final GlobalAuditService globalAuditService;

    @Operation(summary = "Listar todos os logs de auditoria de todas as entidades")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageDTO<GlobalAuditDTO>> getAllAuditLogs(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size) {

        log.info("Recebida requisição para listar todos os logs de auditoria - Página: {}, Tamanho: {}", page, size);

        PageDTO<GlobalAuditDTO> auditLogs = globalAuditService.getGlobalRevisions(page, size);
        return ResponseEntity.ok(auditLogs);
    }
}
