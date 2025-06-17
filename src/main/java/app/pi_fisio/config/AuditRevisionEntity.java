package app.pi_fisio.config;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;


@Entity
@Getter
@Setter
@RevisionEntity(UserRevisionListener.class)
public class AuditRevisionEntity extends DefaultRevisionEntity {
    private String username;
}

