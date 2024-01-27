package ai.soul.requests;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FavourableSlotRequest {
    private List<Long> userIds;
    private Long duration;
    private LocalDate date;
}
