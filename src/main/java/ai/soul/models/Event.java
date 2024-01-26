package ai.soul.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "event",
        indexes = {
                @Index(name = "idx_recurring", columnList = "recurring"),
                @Index(name = "idx_allotted_date", columnList = "allotted_date")
        }
)
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long event_id;
    private LocalDate allotted_date;
    private LocalTime start_time;
    private LocalTime end_time;
    private boolean recurring;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserEvent> user_events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        Event event = (Event) o;
        return Objects.equals(event_id, event.event_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event_id);
    }
}
