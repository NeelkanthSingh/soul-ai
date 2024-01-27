package ai.soul.responses;

import ai.soul.dto.TimePair;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SlotsResponse {
    private LocalDate allottedDate;
    private List<TimePair> timePairs;
    private String message;
}
