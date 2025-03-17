package app.pi_fisio.controller;

import app.pi_fisio.dto.ExercisePageDTO;
import app.pi_fisio.dto.JointIntensityDTO;
import app.pi_fisio.dto.UserDTO;
import app.pi_fisio.dto.UserPageDTO;
import app.pi_fisio.entity.JointIntensity;
import app.pi_fisio.entity.User;
import app.pi_fisio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/api/user")
@Tag(name = "Usuário", description = "Endpoints para gestão de usuários")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Criar usuário", description = "Criação de um novo usuário, disponível apenas para ADMINs.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        log.info("Recebida requisição POST para criar usuário com email: {}", userDTO.getEmail());
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("E-mail não pode estar vazio.");
        }

            UserDTO response = userService.create(userDTO);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(response.getId())
                    .toUri();
            return ResponseEntity.created(location).body(response);

    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente, disponível apenas para ADMINs.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        log.info("Recebida requisição  para atualizar usuário ID: {}", id);
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(userService.update(id, userDTO));
    }

    @Operation(summary = "Excluir usuário", description = "Exclui um usuário pelo ID, disponível apenas para ADMINs.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        log.info("Recebida requisição para excluir usuário ID: {}", id);
       if (id == null || id.toString().isEmpty()){
           return ResponseEntity.badRequest().body("ID não pode ser inválido");
       }
        userService.delete(id);
        return ResponseEntity.ok("User with the id: " + id + " has been deleted!");
    }
    @Operation(summary = "Buscar usuário por ID", description = "Busca um usuário específico pelo ID, disponível apenas para ADMINs.")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getPersonById(@PathVariable Long id) {
        log.info("Recebida requisição para buscar usuário ID: {}", id);
        if (id == null || id.toString().isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Listar usuários", description = "Retorna uma lista paginada de usuários, disponível apenas para ADMINs.")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserPageDTO> getAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int size
    ) throws Exception {
        log.info("Recebida requisição GET para listar usuários - Página: {}, Tamanho: {}", page, size);
        UserPageDTO userPageDTO = userService.findAll(page,size);
        return ResponseEntity.ok(userPageDTO);
    }

    @Operation(summary = "Atualizar usuário autenticado", description = "Atualiza parcialmente os dados do usuário autenticado baseado no JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    })
    @PatchMapping
    public ResponseEntity<UserDTO> patchUpdateByJwt(
            @RequestBody UserDTO userDTO,
            @RequestHeader("Authorization") String authorizationHeader)
            throws Exception{
        log.info("Recebida requisição  para atualizar usuário autenticado.");
        String jwt = authorizationHeader.substring(7);
        UserDTO response = userService.patchUpdate(userDTO, jwt);
        log.info("Usuário atualizado parcialmente com sucesso. ID: {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar usuário autenticado", description = "Retorna as informações do usuário autenticado baseado no JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    })
    @GetMapping("/info")
    public ResponseEntity<UserDTO> getUserByJwt(
            @RequestHeader("Authorization") String authorizationHeader)
            throws Exception{
        log.info("Recebida requisição  para buscar informações do usuário autenticado.");
        String jwt = authorizationHeader.substring(7);
        UserDTO response = userService.findUserByJwt(jwt);
        log.info("Usuário autenticado encontrado: ID {}", response.getId());
        return ResponseEntity.ok(response);
    }
}
