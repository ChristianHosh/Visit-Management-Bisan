package com.example.vm.controller;

import com.example.vm.dto.request.LoginRequest;
import com.example.vm.dto.request.PasswordResetRequest;
import com.example.vm.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/reset_password")
    public ResponseEntity<?> findAllPasswordResetRequests() {
        return authService.findAllPasswordResetRequest();
    }

    @PutMapping("/reset_password/{id}")
    public ResponseEntity<?> acceptResetPassword(@PathVariable(name = "id") Long requestId){
        return authService.acceptResetPasswordRequest(requestId);
    }

    @DeleteMapping("/reset_password/{id}")
    public ResponseEntity<?> rejectResetPassword(@PathVariable(name = "id") Long requestId){
        return authService.rejectResetPasswordRequest(requestId);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<?> requestResetPassword(@RequestBody @Valid PasswordResetRequest passwordResetRequest) {
        return authService.requestPasswordReset(passwordResetRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest ){
        return authService.loginUser(loginRequest);
    }
}
