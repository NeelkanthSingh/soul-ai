package ai.soul.models;

import ai.soul.models.compositeKeys.UserEventID;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_event")
public class UserEvent {

    @EmbeddedId
    UserEventID userEventID;

    @JsonBackReference
    @ManyToOne
    @MapsId("event_id")
    @JoinColumn(name = "event_id")
    private Event event;

    @JsonBackReference
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEvent)) return false;
        UserEvent that = (UserEvent) o;
        return Objects.equals(userEventID, that.userEventID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEventID);
    }
}
