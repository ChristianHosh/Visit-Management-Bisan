package com.example.vm.controller;

import com.example.vm.dto.request.UserPostRequest;
import com.example.vm.dto.request.UserRequest;
import com.example.vm.model.User;
import com.example.vm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers() {

        return userService.findAllUsers();
    }
    @GetMapping("/employees")
    public ResponseEntity<?> findEmployeeUsers(){

        return userService.findEmployeeUsers();
    }

    @GetMapping(value = "/search", params = "query")
    public ResponseEntity<?> searchUsersByQuery(@RequestParam("query") String query) {
        return userService.searchUsersByQuery(query);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable @Valid String username) {
        return userService.findUserByUsername(username);
    }

    @PostMapping("")
    public ResponseEntity<?> saveNewUser(@RequestBody @Valid UserPostRequest userRequest) {
        return userService.saveNewUser(userRequest);
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody @Valid UserRequest userRequest) {
        return userService.updateUser(username, userRequest);
    }

    @PutMapping("/{username}/endis")
    public ResponseEntity<User> enableUser(@PathVariable String username) {
        return userService.enableUser(username);
    }


}