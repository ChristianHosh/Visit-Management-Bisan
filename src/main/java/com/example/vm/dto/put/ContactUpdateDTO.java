package com.example.vm.dto.put;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ContactUpdateDTO {

    @Size(min = 3, max = 30, message = "Invalid firstName: Must be of 3 - 30 characters")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Invalid firstName : Must only contain characters")
    String firstName;

    @Size(min = 3, max = 30, message = "Invalid lastName: Must be of 3 - 30 characters")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Invalid lastName : Must only contain characters")
    String lastName;

    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\d{4}$", message = "Invalid phone number")
    String phoneNumber;

    @Email(message = "Invalid email")
    String email;

    @Null(message = "Invalid enabled : Must be null")
    Integer enabled;
}
