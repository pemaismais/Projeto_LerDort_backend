package app.pi_fisio.repository;

import app.pi_fisio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByKeycloakId(String keycloakId);
}
