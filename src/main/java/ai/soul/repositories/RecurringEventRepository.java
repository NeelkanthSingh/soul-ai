package ai.soul.repositories;

import ai.soul.models.RecurringEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecurringEventRepository extends JpaRepository<RecurringEvent, Long> {
    @Query(value = "Select * from recurring_event where event_id = :eventId", nativeQuery = true)
    RecurringEvent findRecurringEventFromEventId(Long eventId);
}
