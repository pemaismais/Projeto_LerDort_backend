package app.pi_fisio.auth;

//JwtService.java

import app.pi_fisio.config.JwtConfig;
import app.pi_fisio.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Log4j2
@Service
public class JwtService {

    public String generateToken(User userDetails, Integer expiration) throws JWTCreationException {
        String token = JWT.create()
                .withClaim("username", userDetails.getUsername())
                .withClaim("role", userDetails.getRole().name())
                .withClaim("name", userDetails.getName())
                .withClaim("id", userDetails.getId())
                .withClaim("picture", userDetails.getPictureUrl())
                .withIssuer("PI-Fisio")
                .withSubject(userDetails.getUsername())
                .withExpiresAt(generateExpirationDate(expiration))
                .sign(Algorithm.HMAC256(JwtConfig.getSecretKey()));

        log.info("Token JWT gerado com sucesso para usuário: {}", userDetails.getEmail());
        return token;
    }

    public Instant generateExpirationDate(Integer expiration) {
        return LocalDateTime.now()
                .plusMinutes(expiration)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token) throws TokenExpiredException {
        try {
            return JWT.require(Algorithm.HMAC256(JwtConfig.getSecretKey()))
                    .withIssuer("PI-Fisio")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException e) {
            log.warn("Token JWT expirado: {}", token);
            throw e;
        } catch (Exception e) {
            log.error("Erro ao validar o token JWT: {}", e.getMessage());
            throw e;
        }
    }


}
