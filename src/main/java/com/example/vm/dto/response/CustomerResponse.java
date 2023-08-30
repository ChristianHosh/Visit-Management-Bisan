package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerResponse extends ModelAuditResponse {

    private Long id;
    private String name;
    private LocationResponse location;
    private List<ContactResponse> contacts;
}
