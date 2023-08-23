package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.model.City;
import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitForm;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.list.StatusReportListPayload;
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
    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;
    private final VisitDefinitionRepository visitDefinitionRepository;

    @Autowired
    public ReportService(VisitFormRepository visitFormRepository, VisitAssignmentRepository visitAssignmentRepository, VisitTypeRepository visitTypeRepository, CustomerRepository customerRepository, CityRepository cityRepository, UserRepository userRepository, VisitDefinitionRepository visitDefinitionRepository) {
        this.visitFormRepository = visitFormRepository;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.visitTypeRepository = visitTypeRepository;
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.visitDefinitionRepository = visitDefinitionRepository;
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

        long definitionsCount = visitDefinitionRepository.count();

        for (VisitType visitType : visitTypeList) {
            double definitionByTypeCount = visitDefinitionRepository.countVisitDefinitionsByType(visitType);

            System.out.println(visitType.getName());

            System.out.println(definitionByTypeCount);

            double percentage = definitionByTypeCount / definitionsCount;

            System.out.println(definitionsCount);

            customerCountList.add(new CountByTypeListPayload(visitType.getName(), percentage * 100));
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
            area.add(new CustomersInAnAreaListPayload(city.getName(), percentage * 100));
        }

        return ResponseEntity.ok(area);
    }

    public ResponseEntity<List<UserAssignmentReportPayload>> findUserAssignmentByCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        return ResponseEntity.ok(UserAssignmentReportPayload.toPayload(foundCustomer));
    }

    public ResponseEntity<List<UserAverageReportListPayload>> findAverageForAUser() {
        List<User> users = userRepository.searchUsersByAccessLevel(0);
        ArrayList<UserAverageReportListPayload> userAverage = new ArrayList<>();
        for (User user : users) {

            int completedFormsCounter = 0;
            double sumOfTime = 0;

            List<VisitAssignment> visitAssignmentList = visitAssignmentRepository.findVisitAssignmentByUser(user);

            for (VisitAssignment visitAssignment : visitAssignmentList) {
                List<VisitForm> visitFormList = visitFormRepository.findVisitFormByVisitAssignment(visitAssignment);

                for (VisitForm visitForm : visitFormList) {
                    if (visitForm.getStatus().equals(VisitStatus.COMPLETED)) {
                        sumOfTime += (visitForm.getEndTime().getTime() - visitForm.getStartTime().getTime());
                        completedFormsCounter++;

                    }

                }
            }
            System.out.println(user.getUsername() + " : " + sumOfTime + " / " + completedFormsCounter + " = " + (sumOfTime / completedFormsCounter));
            userAverage.add(new UserAverageReportListPayload(user.getUsername(), (sumOfTime / completedFormsCounter) / 1000));
        }


        return ResponseEntity.ok(userAverage);
    }

    public ResponseEntity<List<StatusReportListPayload>> TotalStatusForUser(String username) {
        User founduser = userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));
        long completed = 0;
        long undergoing = 0;
        long not_Started = 0;
        ArrayList<StatusReportListPayload> statusList = new ArrayList<>();
        List<VisitAssignment> visitAssignmentList = visitAssignmentRepository.findVisitAssignmentByUser(founduser);
        for (VisitAssignment visitAssignment : visitAssignmentList) {
            List<VisitForm> visitFormList = visitFormRepository.findVisitFormByVisitAssignment(visitAssignment);
            for (VisitForm visitForm : visitFormList) {
                if (visitForm.getStatus().equals(VisitStatus.COMPLETED)) {
                    ++completed;
                } else if (visitForm.getStatus().equals(VisitStatus.UNDERGOING)) {
                    ++undergoing;
                } else {
                    ++not_Started;
                }
            }

        }
        statusList.add(new StatusReportListPayload(String.valueOf(VisitStatus.COMPLETED), completed));
        statusList.add(new StatusReportListPayload(String.valueOf(VisitStatus.UNDERGOING), undergoing));
        statusList.add(new StatusReportListPayload(String.valueOf(VisitStatus.NOT_STARTED), not_Started));
        return ResponseEntity.ok(statusList);

    }


}
