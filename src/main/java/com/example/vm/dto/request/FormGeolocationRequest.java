package com.example.vm.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormGeolocationRequest {

    private Double longitude;

    private Double latitude;

    private String note;
}
