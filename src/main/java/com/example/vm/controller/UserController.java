package com.example.vm.controller;

import com.example.vm.dto.request.UserPostRequest;
import com.example.vm.dto.request.UserRequest;
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

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEnableUsers() {
        return userService.findAllEnableUsers();
    }

    @GetMapping("/employees")
    public ResponseEntity<?> findEmployeeUsers() {
        return userService.findEmployeeUsers();
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "enabled", required = false) Boolean enabled,
            @RequestParam(value = "role", required = false) Integer role
    ) {
        return userService.searchUsers(name, enabled, role);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable @Valid String username) {
        return userService.findUserByUsername(username);
    }

    @GetMapping("/{username}/visit_assignments")
    public ResponseEntity<?> getUserAssignments(@PathVariable @Valid String username) {
        return userService.findUserAssignments(username);
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
    public ResponseEntity<?> enableUser(@PathVariable String username) {
        return userService.enableUser(username);
    }


}