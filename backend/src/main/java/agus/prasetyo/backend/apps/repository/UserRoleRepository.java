package agus.prasetyo.backend.apps.repository;

import agus.prasetyo.backend.apps.model.db.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<Role, UUID> {

}
