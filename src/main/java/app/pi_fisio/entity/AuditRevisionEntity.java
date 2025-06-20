package app.pi_fisio.entity;

import app.pi_fisio.config.UserRevisionListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.DefaultTrackingModifiedEntitiesRevisionEntity;
import org.hibernate.envers.RevisionEntity;


@Entity
@Getter
@Setter
@RevisionEntity(UserRevisionListener.class)
public class AuditRevisionEntity extends DefaultTrackingModifiedEntitiesRevisionEntity {
    private String username;
}

