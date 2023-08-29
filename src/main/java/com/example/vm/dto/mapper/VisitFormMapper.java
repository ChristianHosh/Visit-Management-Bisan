package com.example.vm.dto.mapper;

import com.example.vm.dto.response.VisitFormResponse;
import com.example.vm.model.VisitForm;

import java.util.List;

public class VisitFormMapper {

    public static VisitFormResponse toListResponse(VisitForm form) {
        if (form == null) return null;

        VisitFormResponse response = new VisitFormResponse();

        response.setId(form.getId());
        response.setStatus(form.getStatus());
        response.setCustomer(CustomerMapper.toListResponse(form.getCustomer()));

        response.setEnabled(form.getEnabled());
        response.setCreatedTime(form.getCreatedTime());
        response.setLastModifiedTime(form.getLastModifiedTime());

        return response;
    }

    public static List<VisitFormResponse> listToRespnoseList(List<VisitForm> visitFormList) {
        if (visitFormList == null) return null;

        return visitFormList
                .stream()
                .map(VisitFormMapper::toListResponse)
                .toList();
    }





}
