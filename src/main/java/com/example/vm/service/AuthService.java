package com.example.vm.service;

import com.example.vm.controller.error.ApiError;
import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.InvalidPasswordResetException;
import com.example.vm.controller.error.exception.PasswordDoesntMatchException;
import com.example.vm.dto.mapper.PasswordResetMapper;
import com.example.vm.dto.mapper.UserMapper;
import com.example.vm.dto.request.LoginRequest;
import com.example.vm.dto.request.PasswordResetRequest;
import com.example.vm.model.PasswordReset;
import com.example.vm.model.User;
import com.example.vm.model.enums.PasswordStatus;
import com.example.vm.repository.PasswordResetRepository;
import com.example.vm.repository.UserRepository;
import com.example.vm.service.util.CalenderDate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

        Timestamp resetTime = new Timestamp(CalenderDate.getTodaySql(-14).getTime());
        if (passwordResetRepository.isRequestStillPending(user))
            throw new InvalidPasswordResetException(ErrorMessage.PASSWORD_REQUEST_STILL_PENDING);
        if (passwordResetRepository.isRequestStillEarly(user, resetTime))
            throw new InvalidPasswordResetException(ErrorMessage.PASSWORD_REQUEST_TOO_EARLY);

        PasswordReset passwordReset = passwordResetRepository.save(PasswordResetMapper.toEntity(passwordResetRequest, user));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(PasswordResetMapper.toResponse(passwordReset));
    }

    public ResponseEntity<?> findAllPasswordResetRequest() {
        List<PasswordReset> passwordResetList = passwordResetRepository.findByStatus(PasswordStatus.PENDING);

        return ResponseEntity.ok(PasswordResetMapper.listToResponseList(passwordResetList));
    }

    public ResponseEntity<?> loginUser(@Valid LoginRequest loginRequest) {
        System.out.println("LOGGING IN: USERNAME: (" + loginRequest.getUsername().trim() + ") PASSWORD: (" + loginRequest.getPassword().trim() + ")");
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(loginRequest.getUsername().trim(), loginRequest.getPassword().trim());

        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError(HttpStatus.UNAUTHORIZED, "Unauthorized: username or password is incorrect"));
        }

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(UserMapper.toListResponse(userOptional.get()));
    }

    public ResponseEntity<?> acceptResetPasswordRequest(Long requestId) {
        PasswordReset passwordReset = passwordResetRepository.findByIdAndStatus(requestId, PasswordStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PASSWORD_RESET_NOT_FOUND));

        passwordReset.setStatus(PasswordStatus.ACCEPTED);
        User user = passwordReset.getUser();

        user.setPassword(passwordReset.getPassword());

        userRepository.save(user);
        passwordResetRepository.save(passwordReset);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> rejectResetPasswordRequest(Long requestId) {
        PasswordReset passwordReset = passwordResetRepository.findByIdAndStatus(requestId, PasswordStatus.PENDING)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PASSWORD_RESET_NOT_FOUND));

        passwordReset.setStatus(PasswordStatus.REJECTED);

        passwordResetRepository.save(passwordReset);

        return ResponseEntity.noContent().build();
    }
}
