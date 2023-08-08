package com.example.vm.dto.post;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Builder
@Data
public class AddressPostDTO {
    @Null(message = "Invalid createdTime : Automatically generated")
    Timestamp createdTime;

    @Null(message = "Invalid lastModifiedTime : Automatically generated")
    Timestamp lastModifiedTime;

    @NotBlank(message = "Invalid Street Address: Empty Street Address ")
    @NotNull(message = "Invalid Street Address : Street Address  is NULL")
    @Size(min = 3, max = 30, message = "Invalid Street Address: Must be of 3 - 30 characters")
    String AddressLine1;

    @NotBlank(message = "Invalid Building Address : Empty Building Address")
    @NotNull(message = "Invalid Building Address : Building Address is NULL")
    @Size(min = 3, max = 30, message = "Invalid firstName: Must be of 3 - 30 characters")
    String AddressLine2;

    @NotNull(message = "Invalid Longitude : Longitude is NULL")
    Double longitude;

    @NotNull(message = "Invalid Latitude : Latitude is NULL")
    Double latitude;

    @NotBlank(message = "Invalid Zipcode : Empty Zipcode")
    @NotNull(message = "Invalid Zipcode : Zipcode is NULL")
    @Size(min = 3, max = 5, message = "Invalid Zipcode: Must be of 3 - 5 characters")
    String zipcode;

    @NotBlank(message = "Invalid City : Empty City")
    @NotNull(message = "Invalid City : City is NULL")
    @Size(min = 3, max = 30, message = "Invalid City: Must be of 3 - 30 characters")
    String city;
}
