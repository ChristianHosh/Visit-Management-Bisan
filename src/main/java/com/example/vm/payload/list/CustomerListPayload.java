package com.example.vm.payload.list;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CustomerListPayload {

    private UUID uuid;

    private String name;

    private int enabled;

}
