package com.example.vm.service;

import com.example.vm.model.visit.VisitType;
import com.example.vm.repository.VisitTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VisitTypeService {

    private final VisitTypeRepository repository;

    @Autowired
    public VisitTypeService(VisitTypeRepository repository) {
        this.repository = repository;
    }

    public VisitType findById(UUID uuid){
        return repository.findById(uuid).orElse(null);
    }
}
