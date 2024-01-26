package ai.soul.services;

import ai.soul.models.Event;
import ai.soul.models.User;
import ai.soul.models.UserEvent;
import ai.soul.models.compositeKeys.UserEventID;
import ai.soul.repositories.EventRepository;
import ai.soul.repositories.RecurringEventRepository;
import ai.soul.repositories.UserEventRepository;
import ai.soul.repositories.UserRepository;
import ai.soul.requests.SlotRequest;
import ai.soul.requests.TimePair;
import ai.soul.responses.SlotBooked;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserEventRepository userEventRepository;
    private final RecurringEventRepository recurringEventRepository;

    @Transactional
    public ResponseEntity<SlotBooked> createSlots(Long userId, SlotRequest slotRequest) {
        LocalDate allottedDate = slotRequest.getAllottedDate();
        List<TimePair> timePairList = slotRequest.getTimePairs();
        User user = userRepository.getReferenceById(userId);
        for (TimePair timePair: timePairList) {

            Event event = Event.builder()
                    .allotted_date(allottedDate)
                    .recurring(false)
                    .start_time(timePair.getStartTime())
                    .end_time(timePair.getEndTime())
                    .build();
            eventRepository.save(event);
            UserEventID userEventID = new UserEventID(user.getUser_id(), event.getEvent_id());
            UserEvent userEvent = UserEvent.builder()
                   .userEventID(userEventID)
                   .user(user)
                   .event(event)
                   .build();

            userEventRepository.save(
                    userEvent
            );

            Set<UserEvent> set = user.getUser_events();
            set.add(userEvent);
            user.setUser_events(set);
            userRepository.save(user);
        }
        SlotBooked slotBooked = SlotBooked.builder()
                .timePairs(timePairList)
                .allottedDate(allottedDate)
                .message("Successfully added all the slots")
                .build();

        return new ResponseEntity<>(slotBooked, HttpStatus.CREATED);
    }



}
