package ai.soul.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class TimePair {
    private LocalTime startTime;
    private LocalTime endTime;
}
