package ai.soul.repositories;

import ai.soul.models.UserEvent;
import ai.soul.models.compositeKeys.UserEventID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserEventRepository extends JpaRepository<UserEvent, UserEventID> {
    @Query(value = "Select event_id from user_event where user_id = :userId", nativeQuery = true)
    List<Long> findEventIdsByUserId(Long userId);
}
