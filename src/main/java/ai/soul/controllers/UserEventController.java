package ai.soul.controllers;

import ai.soul.dto.TimePair;
import ai.soul.requests.*;
import ai.soul.responses.EventConflicts;
import ai.soul.responses.SlotsResponse;
import ai.soul.responses.UserEventsResponse;
import ai.soul.services.UserEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}")
@Slf4j
public class UserEventController {

    private final UserEventService userEventService;

    @PostMapping("/slots")
    public ResponseEntity<SlotsResponse> createSlots(@PathVariable Long userId, @RequestBody SlotsRequest slotsRequest){
        return userEventService.createSlots(userId, slotsRequest);
    }

    @GetMapping("/events")
    public ResponseEntity<UserEventsResponse> fetchEvents(@PathVariable Long userId, @RequestBody UsersRequest usersRequest){
        return userEventService.fetchEvents(usersRequest);
    }

    @PostMapping("/events")
    public ResponseEntity<String> createEvents(@PathVariable Long userId, @RequestBody EventRequest eventRequest){
        return userEventService.createEvents(eventRequest);
    }

    @GetMapping("/conflicts")
    public ResponseEntity<EventConflicts> fetchConflicts(@PathVariable Long userId, @RequestParam LocalDate date){
        return userEventService.fetchConflicts(userId, date);
    }

    @GetMapping("/slots/favourable")
    public ResponseEntity<List<TimePair>> getFavourableSlot(@PathVariable Long userId, @RequestBody FavourableSlotRequest favourableSlotRequest) {
        return userEventService.getFavourableSlot(favourableSlotRequest);
    }

    @PostMapping("/event/recurring")
    public ResponseEntity<String> createRecurringEvent(@PathVariable String userId, @RequestBody RecurringEventRequest recurringEventRequest) {
        return userEventService.createRecurringEvent(recurringEventRequest);
    }

}
