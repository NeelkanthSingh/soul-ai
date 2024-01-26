package ai.soul.repositories;

import ai.soul.models.UserEvent;
import ai.soul.models.compositeKeys.UserEventID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEventRepository extends JpaRepository<UserEvent, UserEventID> {
}
