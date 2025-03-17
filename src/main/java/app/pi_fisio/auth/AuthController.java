package app.pi_fisio.auth;

import app.pi_fisio.auth.RequestAuthDTO;
import app.pi_fisio.auth.RequestRefreshTokenDTO;
import app.pi_fisio.auth.TokenResponseDTO;
import app.pi_fisio.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e geração de tokens JWT")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Autenticação via Google", description = "Realiza autenticação do usuário via token do Google e retorna um JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida", content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido ou não autorizado")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> authWithGoogle(@RequestBody RequestAuthDTO requestAuthDTO) throws Exception {
        String idTokenString = requestAuthDTO.idToken();
        TokenResponseDTO response = authService.authWithGoogle(idTokenString);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Renovação de Token JWT", description = "Gera um novo token de acesso baseado no refresh token fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token atualizado com sucesso", content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
    })
    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponseDTO> authRefreshToken(@RequestBody RequestRefreshTokenDTO refreshTokenDTO) throws Exception {
        String refreshToken = refreshTokenDTO.refreshToken();
        return ResponseEntity.ok(authService.getRefreshToken(refreshToken));
    }
}