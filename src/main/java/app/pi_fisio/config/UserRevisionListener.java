package app.pi_fisio.config;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class UserRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity rev = (AuditRevisionEntity) revisionEntity;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Jwt jwt = (Jwt) auth.getPrincipal();
            rev.setUsername(jwt.getClaims().get("preferred_username").toString());
        } else {
            rev.setUsername("SYSTEM");
        }
    }
}
