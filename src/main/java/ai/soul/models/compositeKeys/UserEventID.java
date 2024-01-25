package ai.soul.models.compositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserEventID implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "event_id")
    Long eventId;
}
