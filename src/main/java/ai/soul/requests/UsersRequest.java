package ai.soul.requests;

import lombok.Data;

import java.util.List;

@Data
public class UsersRequest {
    private List<Long> userIds;
}
