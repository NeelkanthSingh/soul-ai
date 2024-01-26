package ai.soul.controllers;

import ai.soul.models.User;
import ai.soul.repositories.UserRepository;
import ai.soul.requests.SlotRequest;
import ai.soul.responses.SlotBooked;
import ai.soul.services.UserEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}")
public class UserEventController {

    private final UserRepository userRepository;
    private final UserEventService userEventService;

    @PostMapping("/slots")
    public ResponseEntity<SlotBooked> createSlots(@PathVariable Long userId, @RequestBody SlotRequest request){
        return userEventService.createSlots(userId, request);
    }

    @GetMapping("")
    public String createUser(@PathVariable Long userId){
        userRepository.save(
                User.builder().build()
        );
        return "Created User";
    }


}
