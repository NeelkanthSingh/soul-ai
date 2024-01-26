package ai.soul.requests;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimePair {
    private LocalTime startTime;
    private LocalTime endTime;
}
