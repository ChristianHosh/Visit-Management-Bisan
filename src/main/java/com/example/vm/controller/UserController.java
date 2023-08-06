package com.example.vm.controller;

import com.example.vm.model.User;
import com.example.vm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<User>> searchByFirstName(@RequestParam("firstName") String firstName){
        System.out.println("first");
        List<User> userList = userService.searchUsersByFirstName(firstName);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "lastName")
    public ResponseEntity<List<User>> searchByLastName(@RequestParam("lastName") String firstName){
        System.out.println("last");
        List<User> userList = userService.searchUsersByLastName(firstName);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "accessLevel")
    public ResponseEntity<List<User>> searchByAccessLevel(@RequestParam("accessLevel") int accessLevel){
        System.out.println("first");
        List<User> userList = userService.searchUsersByAccessLevel(accessLevel);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> saveNewUser(@RequestBody User newUser) {
        newUser = userService.saveNewUser(newUser);

        if (newUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User userToUpdate) {
        userToUpdate = userService.updateUser(username, userToUpdate);

        if (userToUpdate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userToUpdate, HttpStatus.OK);
    }

}