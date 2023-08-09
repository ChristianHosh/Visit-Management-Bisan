package com.example.vm.dto.put;

import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class AddressPutDTO {

    @Size(min = 3, max = 30, message = "Invalid Address Line : Must be of 3 - 30 characters")
    String AddressLine1;

    @Size(max = 30, message = "Invalid Address Line : Must be a maximum of 30 characters")
    String AddressLine2;

    @Null(message = "Invalid Longitude : Longitude is not NULL")
    Double longitude;

    @Null(message = "Invalid Latitude : Latitude is not NULL")
    Double latitude;

    @Size(min = 3, max = 5, message = "Invalid Zipcode: Must be of 3 - 5 characters")
    String zipcode;

    @Size(min = 3, max = 30, message = "Invalid City: Must be of 3 - 30 characters")
    String city;
}