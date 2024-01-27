package ai.soul.repositories;

import ai.soul.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "Select event_id from event where allotted_date = :allottedDate", nativeQuery = true)
    List<Long> findEventIdsByAllottedDate(LocalDate allottedDate);
}
