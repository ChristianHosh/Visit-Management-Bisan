package com.example.vm.dto.put;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class AddressPutDTO {


    @Size(min = 3, max = 30, message = "Invalid Address Line 1: Must be of 3 - 30 characters")
    String AddressLine1;


    @Size(min = 3, max = 30, message = "Invalid Address Line 1: Must be of 3 - 30 characters")
    String AddressLine2;

    @NotNull(message = "Invalid Longitude : Longitude is NULL")
    Double longitude;

    @NotNull(message = "Invalid Latitude : Latitude is NULL")
    Double latitude;

    @Size(min = 3, max = 5, message = "Invalid Zipcode: Must be of 3 - 5 characters")
    String zipcode;

    @Size(min = 3, max = 30, message = "Invalid City: Must be of 3 - 30 characters")
    String city;
}
