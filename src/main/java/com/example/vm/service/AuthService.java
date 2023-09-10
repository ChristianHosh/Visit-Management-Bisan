package com.example.vm.service;

import com.example.vm.controller.error.ApiError;
import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.PasswordDoesntMatchException;
import com.example.vm.dto.mapper.PasswordResetMapper;
import com.example.vm.dto.request.LoginRequest;
import com.example.vm.dto.request.PasswordResetRequest;
import com.example.vm.dto.response.PasswordResetResponse;
import com.example.vm.model.PasswordReset;
import com.example.vm.model.User;
import com.example.vm.repository.PasswordResetRepository;
import com.example.vm.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordResetRepository passwordResetRepository) {
        this.userRepository = userRepository;
        this.passwordResetRepository = passwordResetRepository;
    }

    public ResponseEntity<?> requestPasswordReset(PasswordResetRequest passwordResetRequest) {
        User user = userRepository.findById(passwordResetRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        if (!passwordResetRequest.getPassword().equals(passwordResetRequest.getConfirmPassword()))
            throw new PasswordDoesntMatchException();

        PasswordReset passwordReset = PasswordResetMapper.toEntity(passwordResetRequest, user);
        passwordReset = passwordResetRepository.save(passwordReset);

        return ResponseEntity.status(HttpStatus.CREATED).body(PasswordResetMapper.toResponse(passwordReset));
    }

    public ResponseEntity<?> findAllPasswordResetRequest() {
        List<PasswordReset> passwordResetList = passwordResetRepository.findAll();

        List<PasswordResetResponse> responseList = PasswordResetMapper.listToResponseList(passwordResetList);

        return ResponseEntity.ok(responseList);
    }

    public ResponseEntity<?> loginUser(@Valid LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());

        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError(HttpStatus.UNAUTHORIZED, "Unauthorized: username or password is incorrect"));

        }

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userOptional.get());
    }
}
