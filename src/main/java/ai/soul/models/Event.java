package ai.soul.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "event",
        indexes = {
                @Index(name = "idx_recurring", columnList = "recurring"),
//                @Index(name = "idx_event_id", columnList = "event_id"),
                @Index(name = "idx_allotted_date", columnList = "allotted_date"),
        }
)
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long event_id;
    private LocalDateTime allotted_date;
    private LocalTime start_time;
    private LocalTime end_time;
    private boolean recurring;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<UserEvent> user_events;
}
