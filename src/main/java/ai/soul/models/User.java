package ai.soul.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cs_user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserEvent> user_events;
}
