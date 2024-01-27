package ai.soul.requests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecurringEventRequest {
    private Long eventId;
    private LocalDate repeatsTill;
}
