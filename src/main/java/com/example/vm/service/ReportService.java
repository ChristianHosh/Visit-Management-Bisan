package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.model.City;
import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitDefinition;
import com.example.vm.model.VisitForm;
import com.example.vm.model.VisitType;
import com.example.vm.payload.report.StatusReportListPayload;
import com.example.vm.payload.report.*;
import com.example.vm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

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
        return ResponseEntity.ok(FormReportListPayload.toPayload(visitFormRepository.findVisitFormsByEnabled(true)));
    }

    public ResponseEntity<List<FormReportListPayload>> getAllFormsByStatus(VisitStatus status) {
        return ResponseEntity.ok(FormReportListPayload.toPayload(visitFormRepository.findVisitFormByStatus(status)));
    }

    public ResponseEntity<List<NamePercentageMapPayload>> getTypesPercentages() {
        ArrayList<NamePercentageMapPayload> customerCountList = new ArrayList<>();

        List<VisitType> visitTypeList = visitTypeRepository.findVisitTypesByEnabledTrue();

        long definitionsCount = visitDefinitionRepository.countVisitDefinitionsByEnabledTrue();

        for (VisitType visitType : visitTypeList) {
            double definitionByTypeCount = visitDefinitionRepository.countVisitDefinitionsByTypeAndEnabledTrue(visitType);

            System.out.println(visitType.getName());

            System.out.println(definitionByTypeCount);

            double percentage = definitionByTypeCount / definitionsCount;

            if (percentage == 0) continue;

            customerCountList.add(new NamePercentageMapPayload(visitType.getName(), percentage * 100));
        }
        return ResponseEntity.ok(customerCountList);
    }

    public ResponseEntity<List<NamePercentageMapPayload>> getCityCustomersPercentage() {
        ArrayList<NamePercentageMapPayload> area = new ArrayList<>();

        List<City> cityList = cityRepository.findCitiesByEnabledTrue();

        long count = customerRepository.countCustomerByEnabled(true);

        for (City city : cityList) {
            double countOfCustomer = customerRepository.countCustomerByAddress_CityAndEnabled(city, true);

            System.out.println(city.getName());
            System.out.println(countOfCustomer);

            double percentage = countOfCustomer / count;

            if (percentage != 0)
                area.add(new NamePercentageMapPayload(city.getName(), percentage * 100));

        }
        return ResponseEntity.ok(area);
    }

    public List<UserAssignmentReportPayload> findUserAssignmentByCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));
        return UserAssignmentReportPayload.toPayload(foundCustomer);
    }

    public ResponseEntity<List<NamePercentageMapPayload>> findAverageTimeForAllUsers() {
        List<User> users = userRepository.searchUsersByAccessLevelAndEnabledTrue(0);

        ArrayList<NamePercentageMapPayload> userAverage = new ArrayList<>();
        for (User user : users) {

            int completedFormsCounter = 0;
            double sumOfTime = 0;

            List<VisitAssignment> visitAssignmentList = visitAssignmentRepository
                    .findVisitAssignmentByUserAndEnabled(user, true);

            for (VisitAssignment visitAssignment : visitAssignmentList) {
                List<VisitForm> visitFormList = visitFormRepository
                        .findVisitFormByVisitAssignmentAndEnabled(visitAssignment, true);

                for (VisitForm visitForm : visitFormList) {
                    if (visitForm.getStatus().equals(VisitStatus.COMPLETED)) {
                        sumOfTime += (visitForm.getEndTime().getTime() - visitForm.getStartTime().getTime());
                        completedFormsCounter++;
                    }

                }
            }
            System.out.println(user.getUsername() + " : " + sumOfTime + " / " + completedFormsCounter + " = " + (sumOfTime / completedFormsCounter));

            userAverage.add(new NamePercentageMapPayload(user.getUsername(), (sumOfTime / completedFormsCounter) / 1000));

        }

        return ResponseEntity.ok(userAverage);
    }

    public ResponseEntity<Map<String, Object>> TotalStatusForUser(String username) {
        User founduser = userRepository.findUserByUsernameAndEnabledTrue(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        List<VisitAssignment> visitAssignmentList = visitAssignmentRepository
                .findVisitAssignmentByUserAndEnabled(founduser, true);
        return ResponseEntity.ok(calculatedStatus(visitAssignmentList));

    }

    public ResponseEntity<Map<String, Object>> TotalStatusForVisitDefinitions(Long id) {
        VisitDefinition foundVisitDefinition = visitDefinitionRepository.findVisitDefinitionByIdAndEnabledTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        List<VisitAssignment> visitAssignmentList = foundVisitDefinition.getVisitAssignments();

        return ResponseEntity.ok(calculatedStatus(visitAssignmentList));
    }

    public Map<String, Object> TotalStatusForCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));
        List<VisitAssignment> visitAssignmentList = foundCustomer.getVisitAssignments();
        return calculatedStatus(visitAssignmentList);
    }

    public Map<String, Object> calculatedStatus(List<VisitAssignment> visitAssignmentList) {
        long completedCount = 0;
        long undergoingCount = 0;
        long notStartedCount = 0;
        long canceledCount = 0;
        long totalFormsCount = 0;

        ArrayList<StatusReportListPayload> countList = new ArrayList<>();
        ArrayList<NamePercentageMapPayload> percentageList = new ArrayList<>();
        for (VisitAssignment visitAssignment : visitAssignmentList) {
            List<VisitForm> visitFormList = visitFormRepository
                    .findVisitFormByVisitAssignmentAndEnabled(visitAssignment, true);
            totalFormsCount += visitFormList.size();

            for (VisitForm visitForm : visitFormList) {
                switch (visitForm.getStatus()) {
                    case COMPLETED -> completedCount++;
                    case UNDERGOING -> undergoingCount++;
                    case NOT_STARTED -> notStartedCount++;
                    case CANCELED -> canceledCount++;
                }
            }
        }

        Iterator<Long> statusCountIterator = Arrays.asList(notStartedCount, undergoingCount, canceledCount, completedCount).iterator();
        Iterator<VisitStatus> statusIterator = Arrays.asList(VisitStatus.values()).iterator();

        while (statusIterator.hasNext() && statusCountIterator.hasNext()) {
            VisitStatus status = statusIterator.next();
            Long count = statusCountIterator.next();

            countList.add(new StatusReportListPayload(String.valueOf(status), count));

            double percentage = (((double) count / (double) totalFormsCount) * 100);

            if (percentage == 0 || totalFormsCount == 0) continue;

            percentageList.add(new NamePercentageMapPayload(String.valueOf(status), percentage));

        }

        Map<String, Object> result = new HashMap<>();
        result.put("count", countList);
        result.put("percentages", percentageList);
        return result;
    }

    public List<NamePercentageMapPayload> calculateTypePercentageForCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        ArrayList<NamePercentageMapPayload> customerCountList = new ArrayList<>();

        List<VisitAssignment> customerAssignmentList = foundCustomer.getVisitAssignments();
        long customerAssignmentListSize = customerAssignmentList.size();

        List<VisitType> visitTypeList = visitTypeRepository.findVisitTypesByEnabledTrue();

        for (VisitType visitType : visitTypeList) {
            long assignmentByTypeCount = 0;

            for (VisitAssignment visitAssignment : customerAssignmentList) {
                if (visitAssignment.getVisitDefinition().getType().equals(visitType)) {
                    assignmentByTypeCount++;
                }
            }

            System.out.println("DEF TYPE COUNT: " + assignmentByTypeCount);

            double percentage = (double) assignmentByTypeCount / (double) customerAssignmentListSize;

            if (percentage == 0 || customerAssignmentListSize == 0) continue;

            customerCountList.add(new NamePercentageMapPayload(visitType.getName(), percentage * 100));

        }
        return customerCountList;
    }

    public ResponseEntity<Map<String, Object>> customerReport(Long id) {
        List<UserAssignmentReportPayload> details = findUserAssignmentByCustomer(id);
        List<NamePercentageMapPayload> percentagesForType = calculateTypePercentageForCustomer(id);
        Map<String, Object> result = new HashMap<>();
        result.put("details", details);
        result.put("percentagesForType", percentagesForType);
        result.putAll(TotalStatusForCustomer(id));
        return ResponseEntity.ok(result);
    }

}
