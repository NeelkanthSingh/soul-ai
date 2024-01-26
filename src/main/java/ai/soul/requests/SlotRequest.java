package ai.soul.requests;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SlotRequest {
    private LocalDate allottedDate;
    private List<TimePair> timePairs;
}


