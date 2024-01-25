package ai.soul.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserEventController {

    @GetMapping("/createEvent")
    public String createEvent(){
        return "Event Created";
    }


}