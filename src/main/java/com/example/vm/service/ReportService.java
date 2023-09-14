package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.mapper.CustomerMapper;
import com.example.vm.dto.mapper.UserMapper;
import com.example.vm.dto.report.CustomerPerformanceResponse;
import com.example.vm.dto.report.UserInteractionResponse;
import com.example.vm.dto.report.UserPerformanceResponse;
import com.example.vm.model.*;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.payload.report.LabelYPayload;
import com.example.vm.payload.report.*;
import com.example.vm.repository.*;
import com.example.vm.service.util.CalenderDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Service
public class ReportService {

    private final VisitFormRepository visitFormRepository;
    private final VisitAssignmentRepository visitAssignmentRepository;
    private final VisitTypeRepository visitTypeRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final VisitDefinitionRepository visitDefinitionRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public ReportService(VisitFormRepository visitFormRepository, VisitAssignmentRepository visitAssignmentRepository, VisitTypeRepository visitTypeRepository, CustomerRepository customerRepository, UserRepository userRepository, VisitDefinitionRepository visitDefinitionRepository,
                         LocationRepository locationRepository) {
        this.visitFormRepository = visitFormRepository;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.visitTypeRepository = visitTypeRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.visitDefinitionRepository = visitDefinitionRepository;
        this.locationRepository = locationRepository;
    }

    public ResponseEntity<List<FormReportListPayload>> getAllForms() {
        return ResponseEntity.ok(FormReportListPayload.toPayload(visitFormRepository.findVisitFormsByEnabled(true)));
    }

    public ResponseEntity<List<FormReportListPayload>> getAllFormsByStatus(VisitStatus status) {
        return ResponseEntity.ok(FormReportListPayload.toPayload(visitFormRepository.findVisitFormByStatus(status)));
    }

    public ResponseEntity<List<NameYPayload>> getTypesPercentages() {
        ArrayList<NameYPayload> customerCountList = new ArrayList<>();

        List<VisitType> visitTypeList = visitTypeRepository.findVisitTypesByEnabledTrue();

        long definitionsCount = visitDefinitionRepository.countVisitDefinitionsByEnabledTrue();

        for (VisitType visitType : visitTypeList) {
            double definitionByTypeCount = visitDefinitionRepository.countVisitDefinitionsByTypeAndEnabledTrue(visitType);

            System.out.println(visitType.getName());

            System.out.println(definitionByTypeCount);

            double percentage = definitionByTypeCount / definitionsCount;

            if (percentage == 0) continue;

            customerCountList.add(new NameYPayload(visitType.getName(), percentage * 100));
        }
        return ResponseEntity.ok(customerCountList);
    }

    public ResponseEntity<List<NameYPayload>> getCityCustomersPercentage() {
        ArrayList<NameYPayload> area = new ArrayList<>();

        List<Location> cityList = locationRepository.findByEnabledTrue();

        long countOfEnabledCustomers = customerRepository.countByEnabledTrue();

        for (Location location : cityList) {
            double countOfCustomer = customerRepository.countByLocationAndEnabledTrue(location);

            double percentage = countOfCustomer / countOfEnabledCustomers;

            if (percentage != 0)
                area.add(new NameYPayload(location.getDetailedLocation(), percentage * 100));

        }
        return ResponseEntity.ok(area);
    }

    public List<UserAssignmentReportPayload> findUserAssignmentByCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));
        return UserAssignmentReportPayload.toPayload(foundCustomer);
    }

    public ResponseEntity<List<NameYPayload>> findAverageTimeForAllUsers() {
        List<User> users = userRepository.searchUsersByAccessLevelAndEnabledTrue(0);

        ArrayList<NameYPayload> userAverage = new ArrayList<>();
        for (User user : users) {

            int completedFormsCounter = 0;
            double sumOfTime = 0;

            List<VisitAssignment> visitAssignmentList = visitAssignmentRepository
                    .findVisitAssignmentByUserAndEnabledTrue(user);

            for (VisitAssignment visitAssignment : visitAssignmentList) {
                List<VisitForm> visitFormList = visitFormRepository
                        .findVisitFormByVisitAssignmentAndEnabledTrue(visitAssignment);

                for (VisitForm visitForm : visitFormList) {
                    if (visitForm.getStatus().equals(VisitStatus.COMPLETED)) {
                        sumOfTime += (visitForm.getEndTime().getTime() - visitForm.getStartTime().getTime());
                        completedFormsCounter++;
                    }

                }
            }
            userAverage.add(new NameYPayload(user.getUsername(), (sumOfTime / completedFormsCounter) / 1000));

        }

        return ResponseEntity.ok(userAverage);
    }

    public ResponseEntity<Map<String, Object>> TotalStatusForUser(String username) {
        User founduser = userRepository.findUserByUsernameAndEnabledTrue(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        List<VisitAssignment> visitAssignmentList = visitAssignmentRepository
                .findVisitAssignmentByUserAndEnabledTrue(founduser);

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

        ArrayList<LabelYPayload> countList = new ArrayList<>();
        ArrayList<NameYPayload> percentageList = new ArrayList<>();

        for (VisitAssignment visitAssignment : visitAssignmentList) {
            List<VisitForm> visitFormList = visitFormRepository
                    .findVisitFormByVisitAssignmentAndEnabledTrue(visitAssignment);
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

            countList.add(new LabelYPayload(String.valueOf(status), count));

            double percentage = (((double) count / (double) totalFormsCount) * 100);

            if ( percentage == 0 || totalFormsCount == 0) continue;

            percentageList.add(new NameYPayload(String.valueOf(status), percentage));

        }

        Map<String, Object> result = new HashMap<>();
        result.put("count", countList);
        result.put("percentages", percentageList);
        return result;
    }

    public List<NameYPayload> calculateTypePercentageForCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        ArrayList<NameYPayload> customerCountList = new ArrayList<>();

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
            double percentage = (double) assignmentByTypeCount / (double) customerAssignmentListSize;

            if (percentage == 0 || customerAssignmentListSize == 0) continue;

            customerCountList.add(new NameYPayload(visitType.getName(), percentage * 100));

        }
        return customerCountList;
    }

    public ResponseEntity<Map<String, Object>> customerReport(Long id) {
        List<UserAssignmentReportPayload> details = findUserAssignmentByCustomer(id);
        List<NameYPayload> percentagesForType = calculateTypePercentageForCustomer(id);
        Map<String, Object> result = new HashMap<>();
        result.put("details", details);
        result.put("percentagesForType", percentagesForType);
        result.putAll(TotalStatusForCustomer(id));
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<List<UserPerformanceResponse>> generateUserPerformanceReport(Date startDate, Date endDate) {
        List<UserPerformanceResponse> userPerformanceReport = userRepository.findByAccessLevelAndEnabledTrue(0)
                .stream()
                .map(user -> generateUserPerformanceResponse(user, startDate, endDate))
                .toList();

        return ResponseEntity.ok(userPerformanceReport);
    }

    public ResponseEntity<?> generateUserDetailedReport(Date startDate, Date endDate, String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        UserPerformanceResponse userPerformance = generateUserPerformanceResponse(user, startDate, endDate);

        List<UserInteractionResponse> userInteractionReport = visitFormRepository.findByVisitAssignment_UserAndVisitAssignment_DateBetweenAndVisitAssignment_EnabledTrueAndEnabledTrue(user, startDate, endDate)
                .stream()
                .map(this::generateUserInteractionResponse)
                .toList();

        Map<String, Object> userDetailedReport = new HashMap<>();
        userDetailedReport.put("performance", userPerformance);
        userDetailedReport.put("interaction", userInteractionReport);

        return ResponseEntity.ok(userDetailedReport);
    }

    public ResponseEntity<?> generateCustomerPerformanceReport(Date startDate, Date endDate) {
        List<CustomerPerformanceResponse> customerPerformanceReport = customerRepository.findCustomerByEnabledTrue()
                .stream()
                .map(customer -> generateCustomerPerformanceResponse(customer, startDate, endDate))
                .filter(Objects::nonNull)
                .toList();

        return ResponseEntity.ok(customerPerformanceReport);
    }

    private UserInteractionResponse generateUserInteractionResponse(VisitForm visitForm) {
        long duration = 0L;
        if (visitForm.getStartTime() != null && visitForm.getEndTime() != null)
            duration = (visitForm.getEndTime().getTime() - visitForm.getStartTime().getTime()) / 1000;

        double longitude = 0L;
        double latitude = 0L;
        if (visitForm.getGeoCoordinates() != null) {
            longitude = visitForm.getGeoCoordinates().getLongitude();
            latitude = visitForm.getGeoCoordinates().getLatitude();
        }

        return UserInteractionResponse
                .builder()
                .customerName(visitForm.getCustomer().getName())
                .customerAddress(visitForm.getCustomer().getLocation().getDetailedLocation())
                .latitude(latitude)
                .longitude(longitude)
                .formStatus(visitForm.getStatus().toString())
                .formType(visitForm.getVisitAssignment().getVisitDefinition().getType().getName())
                .formDuration(duration)
                .formDueDate(visitForm.getVisitAssignment().getDate())
                .build();
    }

    private UserPerformanceResponse generateUserPerformanceResponse(User currentUser, Date startDate, Date endDate) {
        long totalUserAssignmentsCount = visitAssignmentRepository.countByUserAndDateBetweenAndEnabledTrue(currentUser, startDate, endDate);
        long notStartedUserAssignmentsCount = visitAssignmentRepository.countByUserAndDateBetweenAndStatusAndEnabledTrue(currentUser, startDate, endDate, VisitStatus.NOT_STARTED);
        long undergoingUserAssignmentsCount = visitAssignmentRepository.countByUserAndDateBetweenAndStatusAndEnabledTrue(currentUser, startDate, endDate, VisitStatus.UNDERGOING);
        long completedUserAssignmentsCount = visitAssignmentRepository.countByUserAndDateBetweenAndStatusAndEnabledTrue(currentUser, startDate, endDate, VisitStatus.COMPLETED);

        long totalUserFormsCount = visitFormRepository.countByVisitAssignment_UserAndVisitAssignment_DateBetweenAndEnabledTrueAndVisitAssignment_EnabledTrue(currentUser, startDate, endDate);
        long notStartedUserFormsCount = visitFormRepository.countByVisitAssignment_UserAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(currentUser, startDate, endDate, VisitStatus.NOT_STARTED);
        long undergoingUserFormsCount = visitFormRepository.countByVisitAssignment_UserAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(currentUser, startDate, endDate, VisitStatus.UNDERGOING);
        long canceledUserFormsCount = visitFormRepository.countByVisitAssignment_UserAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(currentUser, startDate, endDate, VisitStatus.CANCELED);
        long completedUserFormsCount = visitFormRepository.countByVisitAssignment_UserAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(currentUser, startDate, endDate, VisitStatus.COMPLETED);

        double notStartedUserFormsPercentage = ((double) notStartedUserFormsCount / totalUserFormsCount) * 100;
        double undergoingUserFormsPercentage = ((double) undergoingUserFormsCount / totalUserFormsCount) * 100;
        double canceledUserFormsPercentage = ((double) canceledUserFormsCount / totalUserFormsCount) * 100;
        double completedUserFormsPercentage = ((double) completedUserFormsCount / totalUserFormsCount) * 100;

        long totalTimeCompletingForms = 0;
        List<VisitForm> userCompletedFormList = visitFormRepository.findByVisitAssignment_UserAndVisitAssignment_DateBetweenAndVisitAssignment_EnabledTrueAndEnabledTrueAndStatus(currentUser, startDate, endDate, VisitStatus.COMPLETED);
        for (VisitForm currentForm : userCompletedFormList) {
            totalTimeCompletingForms += currentForm.getEndTime().getTime() - currentForm.getStartTime().getTime();
        }
        double averageTimeCompletingForms = ((double) totalTimeCompletingForms / completedUserFormsCount) / 1000;

        long lateUserFormsCount = 0;
        List<VisitAssignment> userAssignmentList = visitAssignmentRepository.findByUserAndDateBetweenAndEnabledTrue(currentUser, startDate, endDate);
        for (VisitAssignment currentAssignment : userAssignmentList) {
            Date lateDate = CalenderDate.getDateWithOffsetSql(currentAssignment.getDate(), 1);
            Timestamp lateTimestamp = new Timestamp(lateDate.getTime());

            lateUserFormsCount += visitFormRepository.countByVisitAssignmentAndStartTimeAfterAndEnabledTrue(currentAssignment, lateTimestamp);
        }

        return UserPerformanceResponse.builder()
                .totalAssignments(totalUserAssignmentsCount)
                .notStartedAssignments(notStartedUserAssignmentsCount)
                .undergoingAssignments(undergoingUserAssignmentsCount)
                .completedAssignments(completedUserAssignmentsCount)
                .totalForms(totalUserFormsCount)
                .notStartedForms(notStartedUserFormsCount)
                .undergoingForms(undergoingUserFormsCount)
                .canceledForms(canceledUserFormsCount)
                .completedForms(completedUserFormsCount)
                .notStartedFormsPer(notStartedUserFormsPercentage)
                .undergoingFormsPer(undergoingUserFormsPercentage)
                .canceledFormsPer(canceledUserFormsPercentage)
                .completedFormsPer(completedUserFormsPercentage)
                .averageCompletionTime(averageTimeCompletingForms)
                .lateFormsCount(lateUserFormsCount)
                .user(UserMapper.toListResponse(currentUser))
                .build();
    }

    private CustomerPerformanceResponse generateCustomerPerformanceResponse(Customer customer, Date startDate, Date endDate) {
        long totalCustomerFormsCount = visitFormRepository.countByCustomerAndVisitAssignment_DateBetweenAndCustomer_EnabledTrueAndEnabledTrue(customer, startDate, endDate);
        if (totalCustomerFormsCount == 0) return null;

        long notStartedCustomerFormsCount = visitFormRepository.countByCustomerAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(customer, startDate, endDate, VisitStatus.NOT_STARTED);
        long undergoingCustomerFormsCount = visitFormRepository.countByCustomerAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(customer, startDate, endDate, VisitStatus.UNDERGOING);
        long canceledCustomerFormsCount = visitFormRepository.countByCustomerAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(customer, startDate, endDate, VisitStatus.CANCELED);
        long completedCustomerFormsCount = visitFormRepository.countByCustomerAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(customer, startDate, endDate, VisitStatus.COMPLETED);

        double notStartedCustomerFormsPercentage = ((double) notStartedCustomerFormsCount / totalCustomerFormsCount) * 100;
        double undergoingCustomerFormsPercentage = ((double) undergoingCustomerFormsCount / totalCustomerFormsCount) * 100;
        double canceledCustomerFormsPercentage = ((double) canceledCustomerFormsCount / totalCustomerFormsCount) * 100;
        double completedCustomerFormsPercentage = ((double) completedCustomerFormsCount / totalCustomerFormsCount) * 100;

        long totalTimeCompletingForms = 0;
        List<VisitForm> customerCompletedForms = visitFormRepository.findByCustomerAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(customer, startDate, endDate, VisitStatus.COMPLETED);
        for (VisitForm currentForm : customerCompletedForms) {
            totalTimeCompletingForms += currentForm.getEndTime().getTime() - currentForm.getStartTime().getTime();
        }
        double averageTimeCompletingForms = ((double) totalTimeCompletingForms / completedCustomerFormsCount) / 1000;

        List<VisitForm> customerForms = visitFormRepository.findByCustomerAndVisitAssignment_DateBetweenAndEnabledTrueAndVisitAssignment_EnabledTrueAndStartTimeNotNull(customer, startDate, endDate);
        long totalLateCustomerFormsCount = customerForms.stream().filter(currentForm -> {
            Date lateDate = CalenderDate.getDateWithOffsetSql(currentForm.getVisitAssignment().getDate(), 1);
            Timestamp lateTimestamp = new Timestamp(lateDate.getTime());

            return currentForm.getStartTime().after(lateTimestamp);
        }).count();


        return CustomerPerformanceResponse.builder()
                .customer(CustomerMapper.toListResponse(customer))
                .totalForms(totalCustomerFormsCount)
                .notStartedForms(notStartedCustomerFormsCount)
                .undergoingForms(undergoingCustomerFormsCount)
                .canceledForms(canceledCustomerFormsCount)
                .completedForms(completedCustomerFormsCount)
                .notStartedFormsPer(notStartedCustomerFormsPercentage)
                .undergoingFormsPer(undergoingCustomerFormsPercentage)
                .canceledFormsPer(canceledCustomerFormsPercentage)
                .completedFormsPer(completedCustomerFormsPercentage)
                .averageCompletionTime(averageTimeCompletingForms)
                .lateFormsCount(totalLateCustomerFormsCount)
                .build();
    }

}
