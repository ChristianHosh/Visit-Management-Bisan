package com.example.vm.payload.report;

import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitForm;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class FormReportListPayload {

    private Long id;

    private VisitStatus status;

    private Timestamp startTime;


    private Timestamp endTime;

   String name;

   String address;

   Date date;

  String type;


    public static List<FormReportListPayload > toPayload(List<VisitForm> visitformList) {
        return visitformList.stream().map(VisitForm::toListPayloadReport).toList();
    }


}
