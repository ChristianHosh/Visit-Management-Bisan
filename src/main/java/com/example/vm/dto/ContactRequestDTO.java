package com.example.vm.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class ContactRequestDTO {

    @Null(message = "Invalid createdTime : Automatically generated")
    Timestamp createdTime;

    @Null(message = "Invalid lastModifiedTime : Automatically generated")
    Timestamp lastModifiedTime;

    @NotBlank(message = "Invalid firstName: Empty firstName")
    @NotNull(message = "Invalid firstName: firstName is NULL")
    @Size(min = 3, max = 30, message = "Invalid firstName: Must be of 3 - 30 characters")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Invalid firstName : Must only contain characters")
    String firstName;

    @NotBlank(message = "Invalid lastName: Empty lastName")
    @NotNull(message = "Invalid lastName: lastName is NULL")
    @Size(min = 3, max = 30, message = "Invalid lastName: Must be of 3 - 30 characters")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Invalid lastName : Must only contain characters")
    String lastName;

    @NotBlank(message = "Invalid Phone number: Empty number")
    @NotNull(message = "Invalid Phone number: Number is NULL")
    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\d{4}$", message = "Invalid phone number")
    String phoneNumber;

    @Email(message = "Invalid email")
    String email;

    @Null(message = "Invalid enabled : Must be null")
    Integer enabled;


}