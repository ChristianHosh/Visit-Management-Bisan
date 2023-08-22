package com.example.vm.payload.list;

import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitForm;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class VisitFormListPayload {

    private Long id;

    private VisitStatus status;

    private CustomerListPayload customer;

    public static List<VisitFormListPayload > toPayload(List<VisitForm> visitformList) {
        return visitformList.stream().map(VisitForm::toListPayload).toList();
    }
}
