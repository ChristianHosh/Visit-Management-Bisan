package com.example.vm.payload.detail;

import com.example.vm.model.enums.VisitStatus;
import com.example.vm.payload.list.ContactListPayload;
import com.example.vm.payload.list.CustomerListPayload;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class VisitFormDetailPayload {

    private Long id;

    private Timestamp startTime;

    private Timestamp endTime;

    private VisitStatus status;

    private String note;

    private Boolean enabled;

    private CustomerListPayload customer;

    private VisitAssignmentListPayload visitAssignment;

    private List<ContactListPayload> contactList;

}
