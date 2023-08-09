package com.example.vm.dto.put;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerPutDTO {

    @Size(min = 3, max = 30, message = "Invalid Name: Must be of 3 - 30 characters")
    String name;

    @Null(message = "Invalid enabled : Must be null")
    Integer enabled;

    @Valid
    private AddressPutDTO address;
}
