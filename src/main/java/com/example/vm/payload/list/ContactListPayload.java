package com.example.vm.payload.list;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor


public class ContactListPayload {

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private int enabled;


}
