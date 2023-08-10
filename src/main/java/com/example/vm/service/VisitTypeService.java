package com.example.vm.service;

import com.example.vm.dto.post.VisitDefinitionPostDTO;
import com.example.vm.dto.post.VisitTypePostDTO;
import com.example.vm.dto.put.AddressPutDTO;
import com.example.vm.dto.put.CustomerPutDTO;
import com.example.vm.dto.put.VisitTypePutDTO;
import com.example.vm.model.Address;
import com.example.vm.model.Customer;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.model.visit.VisitType;
import com.example.vm.repository.VisitTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class VisitTypeService {

    private final VisitTypeRepository repository;

    @Autowired
    public VisitTypeService(VisitTypeRepository repository) {
        this.repository = repository;
    }

    public VisitType findById(UUID uuid) {
        return repository.findById(uuid).orElse(null);
    }

    public List<VisitType> findAll() {
        return repository.findAll();
    }


    public VisitType saveNewVisitType(VisitTypePostDTO VisitTypeRequest) {

        Timestamp timestamp = Timestamp.from(Instant.now());

        VisitType VisitTypeToSave;


        VisitTypeToSave = VisitType.builder()
                .name(VisitTypeRequest.getName())
                .build();

        VisitTypeToSave.setCreatedTime(timestamp);
        VisitTypeToSave.setLastModifiedTime(timestamp);
        return repository.save(VisitTypeToSave);
    }

    public VisitType updateVisitType(VisitType visitTypeToUpdate, VisitTypePutDTO updatedDTO) {
        Timestamp timestamp = Timestamp.from(Instant.now());
        visitTypeToUpdate.setName(updatedDTO.getName());
        return repository.save(visitTypeToUpdate);
    }
}