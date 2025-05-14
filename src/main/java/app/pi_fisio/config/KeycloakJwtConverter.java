package app.pi_fisio.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class KeycloakJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // 1. Realm roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            roles.forEach(role -> {
                log.debug("Realm role: {}", role);
                authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
            });
        }

        // 2. Client roles (resource_access)
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
                Map<String, Object> clientData = (Map<String, Object>) entry.getValue();
                if (clientData.containsKey("roles")) {
                    List<String> clientRoles = (List<String>) clientData.get("roles");
                    clientRoles.forEach(role -> {
                        log.debug("Client role [{}]: {}", entry.getKey(), role);
                        authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
                    });
                }
            }
        }

        return authorities;
    }
}
