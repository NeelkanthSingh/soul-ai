package ai.soul.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

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

    private LocalDateTime allotted_till_date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", unique = true)
    private Event event;
}
