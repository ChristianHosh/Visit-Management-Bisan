package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.post.VisitTypePostDTO;
import com.example.vm.dto.put.VisitTypePutDTO;
import com.example.vm.model.visit.VisitType;
import com.example.vm.repository.VisitTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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


    public ResponseEntity<VisitType> saveNewVisitType(VisitTypePostDTO VisitTypeRequest) {

        VisitType VisitTypeToSave = VisitType.builder()
                .name(VisitTypeRequest.getName())
                .build();


        VisitTypeToSave = repository.save(VisitTypeToSave);

        return ResponseEntity.ok(VisitTypeToSave);
    }

    public ResponseEntity<VisitType> updateVisitType(Long id, VisitTypePutDTO updatedDTO) {
        VisitType foundVisitType = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        foundVisitType.setName(updatedDTO.getName());

        foundVisitType = repository.save(foundVisitType);

        return ResponseEntity.ok(foundVisitType);
    }
}