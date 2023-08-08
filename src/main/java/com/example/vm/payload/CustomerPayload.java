package com.example.vm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CustomerPayload {

    private UUID uuid;

    private String name;

    private int enabled;
}
