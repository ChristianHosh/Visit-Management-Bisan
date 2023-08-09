package com.example.vm.service;

import com.example.vm.dto.post.VisitDefinitionPostDTO;
import com.example.vm.dto.put.VisitDefinitionPutDTO;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.repository.VisitDefinitionRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class VisitDefinitionService {
    private final VisitDefinitionRepository repository;

    public VisitDefinitionService(VisitDefinitionRepository repository) {
        this.repository = repository;
    }

    public List<VisitDefinition> findAllUsers() {
        return repository.findAll();
    }
    public VisitDefinition saveNewVisit(VisitDefinitionPostDTO VisitDefinitionRequest) {
        Timestamp timestamp = Timestamp.from(Instant.now());
        VisitDefinition VisitDefinitionToSave = VisitDefinition.builder()
                .name(VisitDefinitionRequest.getName())
                .description(VisitDefinitionRequest.getDescription())
                .allowRecurring(VisitDefinitionRequest.getAllowRecurring())
                .frequency(VisitDefinitionRequest.getFrequency())
                .type(VisitDefinitionRequest.getType())
                .enabled(1)
                .build();
        VisitDefinitionToSave.setCreatedTime(timestamp);
        VisitDefinitionToSave.setLastModifiedTime(timestamp);
        return repository.save(VisitDefinitionToSave);
    }

    public VisitDefinition updateVisitDefinition(VisitDefinition VisitDefinitionToUpdate, VisitDefinitionPutDTO updatedDTO) {
        VisitDefinitionToUpdate.setName(updatedDTO.getName());
        VisitDefinitionToUpdate.setType(updatedDTO.getType());
        VisitDefinitionToUpdate.setDescription(updatedDTO.getDescription());
        VisitDefinitionToUpdate.setAllowRecurring(updatedDTO.getAllowRecurring());
        return repository.save(VisitDefinitionToUpdate);
    }

    public VisitDefinition enableUser(VisitDefinition visitDefinition) {
        visitDefinition.setEnabled(visitDefinition.getEnabled() == 0 ? 1 : 0);
        return repository.save(visitDefinition);
    }


}
