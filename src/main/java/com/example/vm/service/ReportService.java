package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.model.City;
import com.example.vm.model.Customer;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.report.CountByTypeListPayload;
import com.example.vm.payload.report.CustomersInAnAreaListPayload;
import com.example.vm.payload.report.FormReportListPayload;
import com.example.vm.payload.report.UserAssignmentReportPayload;
import com.example.vm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final VisitFormRepository visitFormRepository;
    private final VisitTypeRepository visitTypeRepository;
    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;

    @Autowired
    public ReportService(VisitFormRepository visitFormRepository, VisitTypeRepository visitTypeRepository, ContactRepository contactRepository, CustomerRepository customerRepository, CityRepository cityRepository) {
        this.visitFormRepository = visitFormRepository;
        this.visitTypeRepository = visitTypeRepository;
        this.contactRepository = contactRepository;
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
    }

    public ResponseEntity<List<FormReportListPayload>> getAllForms() {
        return ResponseEntity.ok(FormReportListPayload.toPayload(visitFormRepository.findAll()));
    }

    public ResponseEntity<List<FormReportListPayload>> getAllFormsByStatus(VisitStatus status) {
        return ResponseEntity.ok(FormReportListPayload.toPayload(visitFormRepository.findVisitFormByStatus(status)));
    }

    public ResponseEntity<List<CountByTypeListPayload>> getTypesPercentages() {
        ArrayList<CountByTypeListPayload> customerCountList = new ArrayList<>();

        List<VisitType> visitTypeList = visitTypeRepository.findAll();

        long count = contactRepository.count();

        for (VisitType visitType : visitTypeList) {
            double countOfContact = contactRepository.countContactsByVisitTypesContaining(visitType);

            System.out.println(visitType.getName());

            System.out.println(countOfContact);

            double percentage = countOfContact / count;

            System.out.println(count);

            customerCountList.add(new CountByTypeListPayload(visitType.getName(), percentage));
        }
        return ResponseEntity.ok(customerCountList);
    }

    public ResponseEntity<List<CustomersInAnAreaListPayload>> getCityCustomersPercentage() {
        ArrayList<CustomersInAnAreaListPayload> area = new ArrayList<>();

        List<City> cityList = cityRepository.findAll();

        long count = customerRepository.count();

        for (City city : cityList) {
            double countOfCustomer = customerRepository.countCustomerByAddress_City(city);

            System.out.println(city.getName());
            System.out.println(countOfCustomer);

            double percentage = countOfCustomer / count;

            System.out.println(count);
            area.add(new CustomersInAnAreaListPayload(city.getName(), percentage));
        }

        return ResponseEntity.ok(area);
    }

    public ResponseEntity<List<UserAssignmentReportPayload>> findUserAssignmentByCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        return ResponseEntity.ok(UserAssignmentReportPayload.toPayload(foundCustomer));
    }

}
