package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityResponse extends ModelAuditResponse {
    private Long id;
    private String name;
}
