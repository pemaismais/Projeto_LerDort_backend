package app.pi_fisio.controller;

import app.pi_fisio.dto.ExerciseDTO;
import app.pi_fisio.dto.ExerciseFilterDTO;
import app.pi_fisio.dto.ExercisePageDTO;
import app.pi_fisio.entity.Intensity;
import app.pi_fisio.entity.Joint;
import app.pi_fisio.queryfilters.ExerciseQueryFilter;
import app.pi_fisio.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
@Log4j2
@RestController
@RequestMapping("/api/exercise")
@Tag(name = "Exercício", description = "Endpoints para gestão dos Exercícios")
public class ExerciseController {

    @Autowired
    ExerciseService exerciseService;

    @Operation(summary = "Criar um novo exercício", description = "Apenas administradores podem criar exercícios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exercício criado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExerciseDTO> create(@RequestBody ExerciseDTO exerciseDTO) throws Exception {
        log.info("Recebida requisição para criar novo exercício.");
        ExerciseDTO response = exerciseService.create(exerciseDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);

    }

    @Operation(summary = "Atualizar um exercício", description = "Apenas administradores podem atualizar exercícios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercício atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody ExerciseDTO exerciseDTO) throws Exception {
        log.info("Recebida requisição para atualizar exercício ID {}", id);
        exerciseService.update(id, exerciseDTO);
        return ResponseEntity.ok("Exercise with the id: " + id + " has been updated!");

    }

    @Operation(summary = "Deletar um exercício", description = "Apenas administradores podem deletar exercícios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercício deletado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        log.info("Recebida requisição para deletar exercício ID {}", id);
        exerciseService.delete(id);
        return ResponseEntity.ok("Exercise with the id: " + id + " has been deleted!");
    }

    @Operation(summary = "Buscar exercício por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exercício encontrado"),
            @ApiResponse(responseCode = "404", description = "Exercício não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable Long id) throws Exception {
        log.info("Recebida requisição para buscar exercício ID {}", id);
        ExerciseDTO response = exerciseService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todos os exercícios com paginação e filtros")
    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExercisePageDTO> getAll
            (@RequestParam(defaultValue = "0") @PositiveOrZero int page,
             @RequestParam(defaultValue = "10") @Positive @Max(100) int size,
             @ModelAttribute ExerciseQueryFilter filter){
        log.info("Recebida requisição para listar exercícios - Página: {}, Tamanho: {}", page, size);

        ExercisePageDTO response = exerciseService.findAll(page, size, filter);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar exercícios por articulação e intensidade")
    @GetMapping("/findByJointAndIntensity")
    public ResponseEntity<List<ExerciseDTO>> getByJointAndIntensity(@RequestParam Joint joint, @RequestParam Intensity intensity) throws Exception {
        log.info("Recebida requisição para buscar exercícios - Articulação: {}, Intensidade: {}", joint, intensity);
            List<ExerciseDTO> response = exerciseService.findByJointAndIntensity(joint, intensity);
            return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar exercícios recomendados para um usuário")
    @GetMapping("/getByUser")
    public ResponseEntity<List<ExerciseDTO>> getByUser(@RequestParam Long userId) throws Exception {
        log.info("Recebida requisição para buscar exercícios recomendados para usuário ID {}", userId);
        List<ExerciseDTO> response = exerciseService.findByUser(userId);
        return ResponseEntity.ok(response);
    }
}
