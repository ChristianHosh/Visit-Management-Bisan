package com.example.vm.payload.list;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressListPayload {

    private String city;

    private String addressLine1;

    private String addressLine2;

}
