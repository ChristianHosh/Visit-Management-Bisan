package com.example.vm.payload.detail;

import com.example.vm.model.User;
import com.example.vm.payload.list.CustomerListPayload;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class VisitAssignmentDetailPayload {

        private Timestamp createdTime;

        private Timestamp lastModifiedTime;

        private Long id;

        private Date date;

        private String comment;

        private Boolean enabled;

        private List<CustomerListPayload> customers;

        private User user;

}
