package com.example.vm.controller;

import com.example.vm.controller.error.exception.InvalidUserArgumentException;
import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.UserRequestDTO;
import com.example.vm.dto.put.UserUpdateDTO;
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.findAllUsers();

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "firstName")
    public ResponseEntity<List<User>> searchByFirstName(@RequestParam("firstName") String firstName) {
        List<User> userList = userService.searchUsersByFirstName(firstName);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "lastName")
    public ResponseEntity<List<User>> searchByLastName(@RequestParam("lastName") String firstName) {
        List<User> userList = userService.searchUsersByLastName(firstName);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "accessLevel")
    public ResponseEntity<List<User>> searchByAccessLevel(@RequestParam("accessLevel") int accessLevel) {
        if (User.isNotValidAccessLevel(accessLevel))
            throw new InvalidUserArgumentException("ACCESS LEVEL IS NOT VALID, SHOULD BE A 1 OR 0");

        List<User> userList = userService.searchUsersByAccessLevel(accessLevel);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);

        if (user == null)
            throw new UserNotFoundException("USERNAME NOT FOUND : '" + username + "'");


        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> saveNewUser(@RequestBody @Valid UserRequestDTO userRequest) {
        User user = userService.findUserByUsername(userRequest.getUsername().trim());

        if (user != null) {
            throw new UserAlreadyExistsException("USERNAME ALREADY EXISTS!");
        }

        User savedUser = userService.saveNewUser(userRequest);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody @Valid UserUpdateDTO userUpdate) {
        User userToUpdate = userService.findUserByUsername(username);

        if (userToUpdate == null)
            throw new UserNotFoundException("USERNAME NOT FOUND : '" + username + "'");

        User updatedUser = userService.updateUser(userToUpdate, userUpdate);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @PutMapping("/{username}/endis")
    public ResponseEntity<User> enableUser(@PathVariable String username) {
        User userToEnable = userService.findUserByUsername(username);

        if (userToEnable == null)
            throw new UserNotFoundException("USERNAME NOT FOUND : '" + username + "'");

        userToEnable = userService.enableUser(userToEnable);

        return new ResponseEntity<>(userToEnable, HttpStatus.OK);
    }

    private void ValidateUser(User user) {
        if (User.isNotValidAccessLevel(user.getAccessLevel()))
            throw new InvalidUserArgumentException("ACCESS LEVEL IS NOT VALID, SHOULD BE A 1 OR 0");

        if (User.isNotValidEnabled(user.getEnabled()))
            throw new InvalidUserArgumentException("IS ENABLED IS NOT VALID, SHOULD BE A 1 OR 0");

        if (User.isNotValidName(user.getFirstName().trim()))
            throw new InvalidUserArgumentException("FIRST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");

        if (User.isNotValidName(user.getLastName().trim()))
            throw new InvalidUserArgumentException("LAST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");
    }

}