package app.pi_fisio.config;

import app.pi_fisio.entity.User;
import app.pi_fisio.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserSyncInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt token = jwtAuth.getToken();

            String keycloakId = token.getSubject();
            String name = token.getClaimAsString("name");

            userRepository.findByKeycloakId(keycloakId)
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .keycloakId(keycloakId)
                                .name(name)
                                .build();
                        return userRepository.save(newUser);
                    });
        }

        return true;
    }
}
