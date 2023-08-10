package com.example.vm.service;

import com.example.vm.dto.post.VisitDefinitionPostDTO;
import com.example.vm.dto.put.VisitDefinitionPutDTO;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.model.visit.VisitType;
import com.example.vm.repository.VisitDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VisitDefinitionService {
    private final VisitDefinitionRepository repository;

    @Autowired
    public VisitDefinitionService(VisitDefinitionRepository repository) {
        this.repository = repository;
    }

    public List<VisitDefinition> findAllVisitDefinition() {
        return repository.findAll();
    }

    public VisitDefinition findVisitDefinitionByUUID(UUID uuid) {
        Optional<VisitDefinition> VisitDefinitionOptional = repository.findById(uuid);

        return VisitDefinitionOptional.orElse(null);
    }

    public VisitDefinition saveNewVisit(VisitDefinitionPostDTO VisitDefinitionRequest, VisitType visitType) {

        Timestamp timestamp = Timestamp.from(Instant.now());

        VisitDefinition VisitDefinitionToSave;

        if (VisitDefinitionRequest.getAllowRecurring()) {
            VisitDefinitionToSave = VisitDefinition.builder()
                    .name(VisitDefinitionRequest.getName())
                    .description(VisitDefinitionRequest.getDescription())
                    .allowRecurring(VisitDefinitionRequest.getAllowRecurring())
                    .frequency(VisitDefinitionRequest.getFrequency())
                    .type(visitType)
                    .enabled(1)
                    .build();
        } else {
            VisitDefinitionToSave = VisitDefinition.builder()
                    .name(VisitDefinitionRequest.getName())
                    .description(VisitDefinitionRequest.getDescription())
                    .allowRecurring(VisitDefinitionRequest.getAllowRecurring())
                    .frequency(0)
                    .type(visitType)
                    .enabled(1)
                    .build();

        }
        VisitDefinitionToSave.setCreatedTime(timestamp);
        VisitDefinitionToSave.setLastModifiedTime(timestamp);

        return repository.save(VisitDefinitionToSave);
    }

    public VisitDefinition updateVisitDefinition(VisitDefinition VisitDefinitionToUpdate, VisitDefinitionPutDTO updatedDTO, VisitType updatedVisitType) {

        VisitDefinitionToUpdate.setName(updatedDTO.getName());
        VisitDefinitionToUpdate.setType(updatedVisitType);
        VisitDefinitionToUpdate.setDescription(updatedDTO.getDescription());
        VisitDefinitionToUpdate.setAllowRecurring(updatedDTO.getAllowRecurring());

        return repository.save(VisitDefinitionToUpdate);
    }

    public VisitDefinition enableVisitDefinition(VisitDefinition visitDefinition) {
        visitDefinition.setEnabled(visitDefinition.getEnabled() == 0 ? 1 : 0);

        return repository.save(visitDefinition);
    }

    public List<VisitDefinition> searchByDescription(String description) {
        return repository.searchVisitDefinitionsByDescriptionContaining(description);
    }

    //TODO FIX SEARCH BY TYPE
//    public List<VisitDefinition> searchByType(int type) {
//        return repository.searchVisitDefinitionsByType(type);
//    }

    public List<VisitDefinition> searchByFrequency(int frequency) {
        return repository.searchVisitDefinitionsByFrequency(frequency);
    }

    public List<VisitDefinition> searchByAllowRecurring(boolean allowRecurring) {
        return repository.searchVisitDefinitionsByAllowRecurring(allowRecurring);
    }

    public List<VisitDefinition> searchByName(String name) {
        return repository.searchVisitDefinitionsByNameContaining(name);
    }


}
