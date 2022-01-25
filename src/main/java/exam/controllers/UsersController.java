package exam.controllers;

import exam.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/users/{user-id}/block")
    public ResponseEntity<?> blockUser(@PathVariable("user-id") Long userId) {
        usersService.blockUser(userId);
        return ResponseEntity.ok().build();
    }
}
