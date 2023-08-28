package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends ModelAuditResponse{
    private String username;
    private String firstName;
    private String lastName;
    private Integer accessLevel;
}
