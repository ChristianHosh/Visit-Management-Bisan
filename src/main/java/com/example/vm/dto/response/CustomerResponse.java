package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerResponse extends ModelAuditResponse {
    private Long id;
    private String name;
    private AddressResponse address;
    private List<ContactResponse> contacts;
}
