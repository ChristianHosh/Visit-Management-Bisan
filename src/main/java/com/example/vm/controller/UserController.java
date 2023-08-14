package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.UserPostDTO;
import com.example.vm.dto.put.UserPutDTO;
import com.example.vm.model.User;
import com.example.vm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/search", params = "firstName")
    public ResponseEntity<?> searchByFirstName(@RequestParam("firstName") String firstName) {
        return userService.searchUsersByFirstName(firstName);
    }

    @GetMapping(value = "/search", params = "lastName")
    public ResponseEntity<List<User>> searchByLastName(@RequestParam("lastName") String lastName) {
        return userService.searchUsersByLastName(lastName);
    }

    @GetMapping(value = "/search", params = "accessLevel")
    public ResponseEntity<List<User>> searchByAccessLevel(@RequestParam("accessLevel") int accessLevel) {
        return userService.searchUsersByAccessLevel(accessLevel);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable @Valid String username) {
        return userService.findUserByUsername(username);
    }

    @PostMapping("")
    public ResponseEntity<?> saveNewUser(@RequestBody @Valid UserPostDTO userRequest) {
        return userService.saveNewUser(userRequest);
    }

//    @PutMapping("/{username}")
//    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody @Valid UserPutDTO userUpdate) {
//        User userToUpdate = userService.findUserByUsername(username);
//
//        if (userToUpdate == null)
//            throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
//
//        User updatedUser = userService.updateUser(userToUpdate, userUpdate);
//
//        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//    }

//    @PutMapping("/{username}/endis")
//    public ResponseEntity<User> enableUser(@PathVariable String username) {
//        User userToEnable = userService.findUserByUsername(username);
//
//        if (userToEnable == null)
//            throw new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND);
//
//        userToEnable = userService.enableUser(userToEnable);
//
//        return new ResponseEntity<>(userToEnable, HttpStatus.OK);
//    }

}