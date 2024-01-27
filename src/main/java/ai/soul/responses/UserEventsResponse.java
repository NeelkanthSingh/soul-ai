package ai.soul.responses;

import ai.soul.dto.UserEvent;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserEventsResponse {
    private List<UserEvent> userEvents;
}
