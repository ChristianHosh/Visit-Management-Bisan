package com.example.vm.service;


import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.PasswordDoesntMatchException;
import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.dto.mapper.UserMapper;
import com.example.vm.dto.mapper.VisitAssignmentMapper;
import com.example.vm.dto.request.UserPostRequest;
import com.example.vm.dto.request.UserRequest;
import com.example.vm.dto.response.UserResponse;
import com.example.vm.model.User;
import com.example.vm.model.VisitAssignment;
import com.example.vm.repository.UserRepository;
import com.example.vm.repository.VisitAssignmentRepository;
import com.example.vm.service.util.CalenderDate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final VisitAssignmentRepository visitAssignmentRepository;

    @Autowired
    public UserService(UserRepository repository,
                       VisitAssignmentRepository visitAssignmentRepository) {
        this.repository = repository;
        this.visitAssignmentRepository = visitAssignmentRepository;
    }

    public ResponseEntity<List<UserResponse>> findAllUsers() {
        List<User> queryResult = repository.findAll();

        return ResponseEntity.ok(UserMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<List<UserResponse>> findAllEnableUsers() {
        List<User> queryResult = repository.findUsersByEnabledTrue();

        return ResponseEntity.ok(UserMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<UserResponse> findUserByUsername(String username) {
        User foundUser = repository.findById(username.trim())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        return ResponseEntity.ok(UserMapper.toListResponse(foundUser));
    }

    public ResponseEntity<List<UserResponse>> findEmployeeUsers() {
        List<User> queryResult = repository.searchUsersByAccessLevelAndEnabledTrue(0);

        return ResponseEntity.ok(UserMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<UserResponse> saveNewUser(@Valid UserPostRequest userRequest) {
        if (repository.existsById(userRequest.getUsername()))
            throw new UserAlreadyExistsException();

        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword()))
            throw new PasswordDoesntMatchException();

        User userToSave = UserMapper.toEntity(userRequest);

        userToSave = repository.save(userToSave);

        return ResponseEntity.ok(UserMapper.toListResponse(userToSave));
    }


    public ResponseEntity<UserResponse> updateUser(String username, UserRequest userRequest) {
        User userToUpdate = repository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        UserMapper.update(userToUpdate, userRequest);

        userToUpdate = repository.save(userToUpdate);

        return ResponseEntity.ok(UserMapper.toListResponse(userToUpdate));
    }


    public ResponseEntity<UserResponse> enableUser(String username) {
        User foundUser = repository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        foundUser.setEnabled(!foundUser.getEnabled());
        foundUser = repository.save(foundUser);

        return ResponseEntity.ok(UserMapper.toListResponse(foundUser));
    }

    public ResponseEntity<?> findUserAssignments(String username) {
        User foundUser = repository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        List<VisitAssignment> visitAssignments = visitAssignmentRepository.findByUserAndDateAfter(foundUser, CalenderDate.getTodaySql(-7));

        return ResponseEntity.ok(VisitAssignmentMapper.listToResponseList(visitAssignments));
    }

    public ResponseEntity<?> searchUsers(String name, Boolean enabled, Integer role) {
        List<User> userList = repository.searchUsers(name, enabled, role);

        return ResponseEntity.ok(UserMapper.listToResponseList(userList));
    }
}
