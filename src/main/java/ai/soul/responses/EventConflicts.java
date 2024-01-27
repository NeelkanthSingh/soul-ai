package ai.soul.responses;

import ai.soul.dto.Event;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventConflicts {
    List<Event> conflictedEvents;
}
