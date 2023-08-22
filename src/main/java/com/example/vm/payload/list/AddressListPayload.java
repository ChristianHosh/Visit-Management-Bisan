package com.example.vm.payload.list;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressListPayload {

    private Long cityId;

    private String addressLine1;

    private String addressLine2;

    private String zipcode;

}
