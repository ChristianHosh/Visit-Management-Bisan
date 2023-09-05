package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetResponse extends ModelAuditResponse {
    private Long id;
    private String status;
    private UserResponse user;
}
