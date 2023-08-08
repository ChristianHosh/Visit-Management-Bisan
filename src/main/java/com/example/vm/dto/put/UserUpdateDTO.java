package com.example.vm.dto.put;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserUpdateDTO {

    @Null
    String username;

    @Null
    String password;

    @Size(min = 3, max = 30, message = "Invalid firstName: Must be of 3 - 30 characters")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Invalid firstName : Must only contain characters")
    String firstName;

    @Size(min = 3, max = 30, message = "Invalid lastName: Must be of 3 - 30 characters")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Invalid lastName : Must only contain characters")
    String lastName;

    @Min(value = 0, message = "Invalid accessLevel: Equals to zero")
    @Max(value = 1, message = "Invalid accessLevel: Exceeds one")
    Integer accessLevel;

    @Null(message = "Invalid enabled : Must be null")
    Integer enabled;
}
