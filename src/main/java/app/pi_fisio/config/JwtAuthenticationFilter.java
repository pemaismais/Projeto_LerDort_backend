package app.pi_fisio.config;

//JwtAuthenticationFilter.java

import app.pi_fisio.entity.User;
import app.pi_fisio.repository.UserRepository;
import app.pi_fisio.auth.JwtService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
@Log4j2
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException, JWTVerificationException{
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userLogin;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Requisição sem token JWT ou com formato inválido. URI: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);

        try {
            userLogin = jwtService.validateToken(jwt);
            log.info("Token JWT validado para usuário: {}", userLogin);
        } catch (JWTVerificationException e) {
            log.warn("Falha na validação do token JWT: {}. Erro: {}", jwt, e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        Optional<User> optionalUser = userRepository.findByEmail(userLogin);
        if (optionalUser.isEmpty()) {
            log.warn("Usuário não encontrado para o token JWT: {}", userLogin);
            filterChain.doFilter(request, response);
            return;
        }

        User user = optionalUser.get();
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("Autenticação bem-sucedida para o usuário: {}", user.getEmail());
        filterChain.doFilter(request, response);
    }
}
