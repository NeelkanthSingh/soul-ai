package ai.soul.requests;

import ai.soul.dto.TimePair;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EventRequest {
    private LocalDate allottedDate;
    private List<TimePair> timePairs;
    private List<Long> userIds;
}


