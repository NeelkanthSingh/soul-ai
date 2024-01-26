package ai.soul.models.compositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventID implements Serializable {

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "event_id")
    private Long event_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEventID)) return false;
        UserEventID that = (UserEventID) o;
        return Objects.equals(user_id, that.user_id) &&
                Objects.equals(event_id, that.event_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, event_id);
    }
}
