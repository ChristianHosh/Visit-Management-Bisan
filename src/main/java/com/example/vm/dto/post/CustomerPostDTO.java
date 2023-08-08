package com.example.vm.dto.post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class CustomerPostDTO {

    @Null(message = "Invalid createdTime : Automatically generated")
    Timestamp createdTime;

    @Null(message = "Invalid lastModifiedTime : Automatically generated")
    Timestamp lastModifiedTime;

    @NotBlank(message = "Invalid Name: Empty username")
    @NotNull(message = "Invalid Name: Name is NULL")
    @Size(min = 3, max = 30, message = "Invalid Name: Must be of 3 - 30 characters")
    String name;

    @Null(message = "Invalid enabled : Must be null")
    Integer enabled;

    @NotNull(message = "Invalid Address: Address is NULL")
    @Valid
    AddressPostDTO address;
}

