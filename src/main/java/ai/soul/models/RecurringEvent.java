package ai.soul.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recurring_event")
public class RecurringEvent {

    @Id
    @Column(name = "recurring_event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recurring_event_id;

    private LocalDate allotted_till_date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", unique = true)
    private Event event;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecurringEvent)) return false;
        RecurringEvent that = (RecurringEvent) o;
        return Objects.equals(recurring_event_id, that.recurring_event_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recurring_event_id);
    }
}
