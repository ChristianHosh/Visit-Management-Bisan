package com.example.vm.controller;

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

    @GetMapping("reset_password")
    public ResponseEntity<?> findAllPasswordResetRequests() {
        return authService.findAllPasswordResetRequest();
    }

    @PostMapping("reset_password")
    public ResponseEntity<?> requestResetPassword(@RequestBody @Valid PasswordResetRequest passwordResetRequest) {
        return authService.changePassword(passwordResetRequest);
    }
}
