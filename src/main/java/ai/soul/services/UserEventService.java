package ai.soul.services;

import ai.soul.models.Event;
import ai.soul.models.RecurringEvent;
import ai.soul.models.User;
import ai.soul.models.UserEvent;
import ai.soul.models.compositeKeys.UserEventID;
import ai.soul.repositories.EventRepository;
import ai.soul.repositories.RecurringEventRepository;
import ai.soul.repositories.UserEventRepository;
import ai.soul.repositories.UserRepository;
import ai.soul.requests.*;
import ai.soul.dto.TimePair;
import ai.soul.responses.EventConflicts;
import ai.soul.responses.SlotsResponse;
import ai.soul.responses.UserEventsResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserEventRepository userEventRepository;
    private final RecurringEventRepository recurringEventRepository;

    @Transactional
    public ResponseEntity<SlotsResponse> createSlots(Long userId, SlotsRequest slotsRequest) {
        LocalDate allottedDate = slotsRequest.getAllottedDate();
        List<TimePair> timePairList = slotsRequest.getTimePairs();
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
        SlotsResponse slotsResponse = SlotsResponse.builder()
                .timePairs(timePairList)
                .allottedDate(allottedDate)
                .message("Successfully added all the slots")
                .build();

        return new ResponseEntity<>(slotsResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<UserEventsResponse> fetchEvents(UsersRequest usersRequest) {
        UserEventsResponse userEventsResponse = UserEventsResponse.builder().build();
        List<ai.soul.dto.UserEvent> userEvents = new ArrayList<>();
        for (Long userid: usersRequest.getUserIds()) {
            List<Long> eventIds = userEventRepository.findEventIdsByUserId(userid);
            ai.soul.dto.UserEvent userEvent = ai.soul.dto.UserEvent.builder().userId(userid).build();
            List<ai.soul.dto.Event> events = new ArrayList<>();
            for (Long eventId: eventIds) {
                Event event = eventRepository.getReferenceById(eventId);
                events.add(ai.soul.dto.Event
                        .builder()
                                .event_id(event.getEvent_id())
                                .allotted_date(event.getAllotted_date())
                                .start_time(event.getStart_time())
                                .end_time(event.getEnd_time())
                                .recurring(event.isRecurring())
                        .build());
            }
            userEvent.setEvents(events);
            userEvents.add(userEvent);
        }
        userEventsResponse.setUserEvents(userEvents);
        return new ResponseEntity<>(userEventsResponse, HttpStatus.OK);
    }

    public ResponseEntity<EventConflicts> fetchConflicts(Long userId, LocalDate date) {
        List<Long> eventIdsForAUser = userEventRepository.findEventIdsByUserId(userId);
        List<Event> eventsForAUserOnADate = new ArrayList<>();
        for (Long eventId: eventIdsForAUser) {
            Event event = eventRepository.getReferenceById(eventId);
            if (event.getAllotted_date().equals(date))
                eventsForAUserOnADate.add(event);
            else if (event.isRecurring()){
                RecurringEvent recurringEvent = recurringEventRepository.findRecurringEventFromEventId(eventId);
                if((recurringEvent.getAllotted_till_date().equals(date) ||
                                recurringEvent.getAllotted_till_date().isAfter(date)))
                    eventsForAUserOnADate.add(event);
            }
        }
        Collections.sort(eventsForAUserOnADate, Comparator.comparing(Event::getStart_time));

        List<Event> conflictingEvents = new ArrayList<>();
        List<Event> currentConflictSet = new ArrayList<>();
        currentConflictSet.add(eventsForAUserOnADate.get(0));

        for (int i = 1; i < eventsForAUserOnADate.size(); i++) {
            Event currentEvent = eventsForAUserOnADate.get(i);
            Event lastEventInConflictSet = currentConflictSet.get(currentConflictSet.size() - 1);

            if (currentEvent.getStart_time().isBefore(lastEventInConflictSet.getEnd_time())) {
                currentConflictSet.add(currentEvent);
            } else {
                if (currentConflictSet.size() > 1) {
                    conflictingEvents.addAll(new ArrayList<>(currentConflictSet));
                }

                currentConflictSet.clear();
                currentConflictSet.add(currentEvent);
            }
        }

        if (currentConflictSet.size() > 1) {
            conflictingEvents.addAll(currentConflictSet);
        }

        List<ai.soul.dto.Event> conflictedEvents = new ArrayList<>();
        for (Event e: conflictingEvents) {
            ai.soul.dto.Event event = ai.soul.dto.Event.builder()
                    .event_id(e.getEvent_id())
                    .allotted_date(e.getAllotted_date())
                    .start_time(e.getStart_time())
                    .end_time(e.getEnd_time())
                    .recurring(e.isRecurring())
                    .build();
            conflictedEvents.add(event);
        }
        EventConflicts eventConflicts = EventConflicts.builder().conflictedEvents(conflictedEvents).build();
        return new ResponseEntity<>(eventConflicts, HttpStatus.OK);
    }

    public ResponseEntity<List<TimePair>> getFavourableSlot(FavourableSlotRequest favourableSlotRequest) {
        List<Event> eventsOfAllUsers = new ArrayList<>();
        for (Long userid: favourableSlotRequest.getUserIds()) {
            List<Long> eventIdsForAUser = userEventRepository.findEventIdsByUserId(userid);
            List<Event> eventsForAUserOnADate = new ArrayList<>();
            for (Long eventId: eventIdsForAUser) {
                Event event = eventRepository.getReferenceById(eventId);
                LocalDate date = favourableSlotRequest.getDate();
                if (event.getAllotted_date().equals(date))
                    eventsForAUserOnADate.add(event);
                else if (event.isRecurring()){
                    RecurringEvent recurringEvent = recurringEventRepository.findRecurringEventFromEventId(eventId);
                    if((recurringEvent.getAllotted_till_date().equals(date) ||
                            recurringEvent.getAllotted_till_date().isAfter(date)))
                        eventsForAUserOnADate.add(event);
                }
            }
            eventsOfAllUsers.addAll(eventsForAUserOnADate);
        }
        Collections.sort(eventsOfAllUsers, Comparator.comparing(Event::getStart_time));

        LocalTime startOfDay = LocalTime.of(0, 0);

        List<TimePair> favourableSlots = new ArrayList<>();

        for (Event event : eventsOfAllUsers) {
            LocalTime eventStart = event.getStart_time();

            if (Duration.between(startOfDay, eventStart).toMinutes() > favourableSlotRequest.getDuration()) {
                favourableSlots.add(TimePair
                        .builder()
                        .startTime(startOfDay)
                        .endTime(eventStart)
                        .build());
            }

            if (favourableSlots.size() > 5) break;
            if (event.getEnd_time().isAfter(startOfDay)) {
                startOfDay = event.getEnd_time();
            }
        }

        if(favourableSlots.size()<5) {
            LocalTime endOfDay = LocalTime.of(23, 59);
            if (Duration.between(startOfDay, endOfDay).toMinutes() > favourableSlotRequest.getDuration()) {
                favourableSlots.add(
                        TimePair
                                .builder()
                                .startTime(startOfDay)
                                .endTime(endOfDay)
                                .build());
            }
        }

    if(favourableSlots.isEmpty())
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

    return new ResponseEntity<>(favourableSlots, HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<String> createEvents(EventRequest eventRequest) {

        LocalDate allottedDate = eventRequest.getAllottedDate();
        List<TimePair> timePairList = eventRequest.getTimePairs();
        for (TimePair timePair: timePairList) {

            Event event = Event.builder()
                    .allotted_date(allottedDate)
                    .recurring(false)
                    .start_time(timePair.getStartTime())
                    .end_time(timePair.getEndTime())
                    .build();
            eventRepository.save(event);

            for (Long userid: eventRequest.getUserIds()) {
                UserEventID userEventID = new UserEventID(userid, event.getEvent_id());
                User user = userRepository.getReferenceById(userid);
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
        }
        return new ResponseEntity<>("Successfully added event to all users", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> createRecurringEvent(RecurringEventRequest recurringEventRequest) {
        Event event = eventRepository.getReferenceById(recurringEventRequest.getEventId());
        event.setRecurring(true);
        eventRepository.save(event);
        RecurringEvent recurringEvent = RecurringEvent
                .builder()
                .event(event)
                .allotted_till_date(recurringEventRequest.getRepeatsTill())
                .build();
        recurringEventRepository.save(
                recurringEvent
        );

        return new ResponseEntity<>("Successfully changed the event to a recurring one", HttpStatus.CREATED);
    }
}
