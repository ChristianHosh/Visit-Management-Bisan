package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.mapper.VisitTypeMapper;
import com.example.vm.dto.request.SimpleNameRequest;
import com.example.vm.dto.response.VisitTypeResponse;
import com.example.vm.model.VisitType;
import com.example.vm.repository.VisitTypeRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitTypeService {

    private final VisitTypeRepository repository;

    @Autowired
    public VisitTypeService(VisitTypeRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<VisitTypeResponse>> findAll() {
        List<VisitType> queryResult = repository.findAll();

        return ResponseEntity.ok(VisitTypeMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<List<VisitTypeResponse>> findAllEnablesTypes() {
        List<VisitType> queryResult = repository.findVisitTypesByEnabledTrue();

        return ResponseEntity.ok(VisitTypeMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<VisitTypeResponse> saveNewVisitType(SimpleNameRequest simpleNameRequest) {
        VisitType visitTypeToSave = VisitTypeMapper.toEntity(simpleNameRequest);

        visitTypeToSave = repository.save(visitTypeToSave);

        return ResponseEntity.ok(VisitTypeMapper.toListResponse(visitTypeToSave));
    }

    public ResponseEntity<VisitTypeResponse> updateVisitType(Long id, SimpleNameRequest simpleNameRequest) {
        VisitType foundVisitType = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        VisitTypeMapper.update(foundVisitType, simpleNameRequest);

        foundVisitType = repository.save(foundVisitType);

        return ResponseEntity.ok(VisitTypeMapper.toListResponse(foundVisitType));
    }

    @NotNull
    public List<VisitType> getVisitTypes(List<Long> typeIdList) {
        List<VisitType> visitTypes = new ArrayList<>();

        for (Long typeId : typeIdList) {
            VisitType visitType = repository.findById(typeId)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TYPE_NOT_FOUND));

            visitTypes.add(visitType);
        }
        return visitTypes;
    }
}