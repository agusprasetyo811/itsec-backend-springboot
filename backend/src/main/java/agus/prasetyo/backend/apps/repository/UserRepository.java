package agus.prasetyo.backend.apps.repository;

import agus.prasetyo.backend.apps.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByIdAndOtp(UUID id, String otp);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
