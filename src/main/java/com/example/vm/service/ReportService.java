package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.model.City;
import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitForm;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.report.*;
import com.example.vm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final VisitFormRepository visitFormRepository;
    private final VisitAssignmentRepository visitAssignmentRepository;
    private final VisitTypeRepository visitTypeRepository;
    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReportService(VisitFormRepository visitFormRepository, VisitAssignmentRepository visitAssignmentRepository, VisitTypeRepository visitTypeRepository, ContactRepository contactRepository, CustomerRepository customerRepository, CityRepository cityRepository, UserRepository userRepository) {
        this.visitFormRepository = visitFormRepository;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.visitTypeRepository = visitTypeRepository;
        this.contactRepository = contactRepository;
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
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

    public ResponseEntity<List<VisitForm>> findAverageForAUser(String id) {
        User founduser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));
        ArrayList<VisitForm>visitForms=new ArrayList<>();
        List<VisitAssignment>List=visitAssignmentRepository.findVisitAssignmentByUser(founduser);
       for(int i=0;i<List.size();++i){
          List <VisitForm> ListForm=visitFormRepository.findVisitFormByVisitAssignment(List.get(i));
         for(int j=0;j<ListForm.size();++j){
             visitForms.add(ListForm.get(j));
         }
       }
       return ResponseEntity.ok(visitForms) ;
    }

}
