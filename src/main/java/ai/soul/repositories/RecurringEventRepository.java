package ai.soul.repositories;

import ai.soul.models.RecurringEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringEventRepository extends JpaRepository<RecurringEvent, Long> {
}
