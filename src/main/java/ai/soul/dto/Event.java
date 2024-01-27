package ai.soul.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class Event {
    private Long event_id;
    private LocalDate allotted_date;
    private LocalTime start_time;
    private LocalTime end_time;
    private Boolean recurring;
}
