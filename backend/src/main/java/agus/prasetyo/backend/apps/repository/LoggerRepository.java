package agus.prasetyo.backend.apps.repository;

import agus.prasetyo.backend.apps.model.db.Logger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoggerRepository extends JpaRepository<Logger, UUID> {
}
