package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.request.VisitTypeRequest;
import com.example.vm.model.visit.VisitType;
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

    public ResponseEntity<List<VisitType>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }
    public ResponseEntity<List<VisitType>> findAllEnablesTypes() {
        return ResponseEntity.ok(repository.findVisitTypesByEnabled(true));
    }

    public ResponseEntity<VisitType> saveNewVisitType(VisitTypeRequest VisitTypeRequest) {

        VisitType VisitTypeToSave = VisitType.builder()
                .name(VisitTypeRequest.getName())
                .build();


        VisitTypeToSave = repository.save(VisitTypeToSave);

        return ResponseEntity.ok(VisitTypeToSave);
    }

    public ResponseEntity<VisitType> updateVisitType(Long id, VisitTypeRequest updatedDTO) {
        VisitType foundVisitType = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        foundVisitType.setName(updatedDTO.getName());

        foundVisitType = repository.save(foundVisitType);

        return ResponseEntity.ok(foundVisitType);
    }

    @NotNull
    public List<VisitType> getVisitTypes(List<Long> typeIdList) {
        List<VisitType> visitTypes = new ArrayList<>();

        for (Long typeId : typeIdList) {
            VisitType visitType = repository.findById(typeId)
                    .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.TYPE_NOT_FOUND));

            visitTypes.add(visitType);
        }
        return visitTypes;
    }
}