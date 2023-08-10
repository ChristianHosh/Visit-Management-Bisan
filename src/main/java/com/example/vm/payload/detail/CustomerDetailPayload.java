package com.example.vm.payload.detail;

import com.example.vm.model.Address;
import com.example.vm.model.Contact;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CustomerDetailPayload {

    private Timestamp createdTime;

    private Timestamp lastModifiedTime;

    private UUID uuid;

    private String name;

    private int enabled;

    private Address address;

    private List<Contact> contacts;

    private List<VisitAssignmentListPayload> visitAssignments;

}
