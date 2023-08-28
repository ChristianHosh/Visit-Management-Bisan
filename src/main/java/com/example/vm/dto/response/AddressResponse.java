package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse extends ModelAuditResponse {
    private String addressLine1;
    private String addressLine2;
    private String zipcode;
    private Double longitude;
    private Double latitude;
    private Long cityId;
    private String cityName;
}
