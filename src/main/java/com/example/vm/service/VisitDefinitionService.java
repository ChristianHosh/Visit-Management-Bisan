package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.InvalidDateException;
import com.example.vm.dto.mapper.VisitDefinitionMapper;
import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.dto.request.VisitDefinitionRequest;
import com.example.vm.dto.response.VisitDefinitionResponse;
import com.example.vm.model.City;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitDefinition;
import com.example.vm.model.VisitType;
import com.example.vm.repository.CityRepository;
import com.example.vm.repository.VisitDefinitionRepository;
import com.example.vm.repository.VisitTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class VisitDefinitionService {
    private final VisitDefinitionRepository visitDefinitionRepository;
    private final VisitTypeRepository visitTypeRepository;
    private final CityRepository cityRepository;

    @Autowired
    public VisitDefinitionService(VisitDefinitionRepository visitDefinitionRepository, VisitTypeRepository visitTypeRepository,
                                  CityRepository cityRepository) {
        this.visitDefinitionRepository = visitDefinitionRepository;
        this.visitTypeRepository = visitTypeRepository;
        this.cityRepository = cityRepository;
    }

    public ResponseEntity<List<VisitDefinitionResponse>> findAllVisitDefinition() {
        List<VisitDefinition> queryResult = visitDefinitionRepository.findAll();

        return ResponseEntity.ok(VisitDefinitionMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<List<VisitDefinitionResponse>> findAllEnabledVisitDefinitions() {
        List<VisitDefinition> queryResult = visitDefinitionRepository.findVisitDefinitionsByEnabledTrue();

        return ResponseEntity.ok(VisitDefinitionMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<VisitDefinitionResponse> findVisitDefinitionByID(Long id) {
        VisitDefinition foundVisitDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        return ResponseEntity.ok(VisitDefinitionMapper.toDetailedResponse(foundVisitDefinition));

    }

    public ResponseEntity<VisitDefinitionResponse> saveNewVisit(VisitDefinitionRequest visitDefinitionRequest) {
        VisitType foundVisitType = visitTypeRepository.findByIdAndEnabledTrue(visitDefinitionRequest.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TYPE_NOT_FOUND));

        City foundCity = cityRepository.findByIdAndEnabledTrue(visitDefinitionRequest.getCityId())
                .orElseThrow( () -> new EntityNotFoundException(ErrorMessage.CITY_NOT_FOUND));

        VisitDefinition visitDefinitionToSave = VisitDefinition.builder()
                .name(visitDefinitionRequest.getName())
                .description(visitDefinitionRequest.getDescription())
                .visitAssignments(new ArrayList<>())
                .type(foundVisitType)
                .city(foundCity)
                .allowRecurring(visitDefinitionRequest.getAllowRecurring())
                .frequency(visitDefinitionRequest.getAllowRecurring() ? visitDefinitionRequest.getFrequency() : 0)
                .build();

        visitDefinitionToSave = visitDefinitionRepository.save(visitDefinitionToSave);

        return ResponseEntity.ok(VisitDefinitionMapper.toDetailedResponse(visitDefinitionToSave));
    }

    public ResponseEntity<VisitDefinitionResponse> updateVisitDefinition(Long id, VisitDefinitionRequest visitDefinitionRequest) {
        VisitDefinition foundDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        VisitType foundVisitType = visitTypeRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TYPE_NOT_FOUND));

        City foundCity = cityRepository.findByIdAndEnabledTrue(visitDefinitionRequest.getCityId())
                .orElseThrow( () -> new EntityNotFoundException(ErrorMessage.CITY_NOT_FOUND));


        foundDefinition.setName(visitDefinitionRequest.getName());
        foundDefinition.setDescription(visitDefinitionRequest.getDescription());
        foundDefinition.setType(foundVisitType);
        foundDefinition.setCity(foundCity);
        foundDefinition.setAllowRecurring(visitDefinitionRequest.getAllowRecurring());
        foundDefinition.setFrequency(visitDefinitionRequest.getAllowRecurring() ? visitDefinitionRequest.getFrequency() : 0);

        foundDefinition = visitDefinitionRepository.save(foundDefinition);

        return ResponseEntity.ok(VisitDefinitionMapper.toDetailedResponse(foundDefinition));
    }

    public ResponseEntity<VisitDefinitionResponse> enableVisitDefinition(Long id) {
        VisitDefinition foundDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        foundDefinition.setEnabled(!foundDefinition.getEnabled());

        boolean isEnabled = foundDefinition.getEnabled();

        foundDefinition.getVisitAssignments()
                .forEach(visitAssignment -> visitAssignment.setEnabled(isEnabled));

        foundDefinition = visitDefinitionRepository.save(foundDefinition);

        return ResponseEntity.ok(VisitDefinitionMapper.toDetailedResponse(foundDefinition));

    }

    public ResponseEntity<List<VisitDefinitionResponse>> searchByQuery(String query) {
        List<VisitDefinition> result = new ArrayList<>();

        List<VisitDefinition> list1 = visitDefinitionRepository.searchVisitDefinitionsByNameContaining(query);
        List<VisitDefinition> list2 = new ArrayList<>();

        try {
            list2 = visitDefinitionRepository.searchVisitDefinitionsByFrequency(Integer.parseInt(query));
        } catch (NumberFormatException ignored) {
        }

        result.addAll(list1);
        result.addAll(list2);

        return ResponseEntity.ok(VisitDefinitionMapper.listToResponseList(result));
    }

    public ResponseEntity<List<VisitDefinitionResponse>> searchByType(Long id) {
        VisitType foundType = visitTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TYPE_NOT_FOUND));

        List<VisitDefinition> visitDefinitionList = visitDefinitionRepository.searchVisitDefinitionsByType(foundType);

        return ResponseEntity.ok(VisitDefinitionMapper.listToResponseList(visitDefinitionList));
    }

    public ResponseEntity<VisitDefinitionResponse> saveNewVisitAssignmentToDefinition(Long id, VisitAssignmentRequest visitAssignmentRequest) {

        VisitDefinition visitDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        // FIND DATE MAKE SURE IT HAS NOT REACHED THAT DATE
        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);
        calendar.add(Calendar.DATE, -1);
        todayDate.setTime(calendar.getTime().getTime());

        // VALIDATES THE DATE TO MAKE SURE IT IN THE PRESENT OR FUTURE
        if (visitAssignmentRequest.getDate().before(todayDate))
            throw new InvalidDateException(ErrorMessage.DATE_IN_PAST);

        VisitAssignment visitAssignment = VisitAssignment.builder()
                .comment(visitAssignmentRequest.getComment())
                .date(visitAssignmentRequest.getDate())
                .build();

        visitAssignment.setVisitDefinition(visitDefinition);
        visitDefinition.getVisitAssignments().add(visitAssignment);
        visitDefinition = visitDefinitionRepository.save(visitDefinition);

        return ResponseEntity.status(HttpStatus.CREATED).body(VisitDefinitionMapper.toDetailedResponse(visitDefinition));
    }
}
