package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponse extends ModelAuditResponse {
    private Long id;
    private String address;
    private Long cityId;
    private String cityName;
    private Double longitude;
    private Double latitude;
}
