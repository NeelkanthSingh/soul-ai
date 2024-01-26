package ai.soul.responses;

import ai.soul.requests.TimePair;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SlotBooked {
    private LocalDate allottedDate;
    private List<TimePair> timePairs;
    private String message;
}
