package ai.soul.controllers;

import ai.soul.models.User;
import ai.soul.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("")
    @Transactional
    public ResponseEntity<String> createUser(){
        userRepository.save(
                User.builder().build()
        );
        return new ResponseEntity<>("Created User", HttpStatus.CREATED);
    }
}
