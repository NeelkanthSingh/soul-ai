package ai.soul.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserEvent {
    private Long userId;
    private List<Event> events;
}
