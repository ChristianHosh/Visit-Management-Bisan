package com.example.vm.service;

import com.example.vm.model.Customer;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.payload.report.LabelYPayload;
import com.example.vm.repository.CustomerRepository;
import com.example.vm.repository.VisitFormRepository;
import com.example.vm.service.util.CalenderDate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class DashboardService {

    private final VisitFormRepository visitFormRepository;
    private final CustomerRepository customerRepository;

    public DashboardService(VisitFormRepository visitFormRepository,
                            CustomerRepository customerRepository) {
        this.visitFormRepository = visitFormRepository;
        this.customerRepository = customerRepository;
    }

    public ResponseEntity<?> topBarCounts() {
        Map<String, Object> resultsMap = new HashMap<>();

        // TODAY'S NUMBER OF COMPLETED FORMS / TOTAL FORMS
        long totalFormsForToday = visitFormRepository.countByVisitAssignment_Date(CalenderDate.getTodaySql());
        long completedFormsForToday = visitFormRepository.countByVisitAssignment_DateAndStatus(CalenderDate.getTodaySql(), VisitStatus.COMPLETED);

        resultsMap.put("totalToday", totalFormsForToday);
        resultsMap.put("completedToday", completedFormsForToday);

        // THIS WEEK'S NUMBER OF CANCELED FORMS
        long canceledFormsForThisWeek = visitFormRepository.countByVisitAssignment_DateBetweenAndStatus(CalenderDate.getTodaySql(-7), CalenderDate.getTodaySql(), VisitStatus.CANCELED);
        resultsMap.put("canceledFormsThisWeek", canceledFormsForThisWeek);

        // THIS MONTH'S CUSTOMER GROWTH NUMBERS / INCREASE % SINCE LAST MONTH
        long newCustomersThisMonth = customerRepository.countByCreatedTimeBetween(CalenderDate.asTimestamp(CalenderDate.getTodaySql(-30)), CalenderDate.asTimestamp(CalenderDate.getTodaySql(1)));
        List<Customer> customerList = customerRepository.findByCreatedTimeBetween(CalenderDate.asTimestamp(CalenderDate.getTodaySql(-30)), CalenderDate.asTimestamp(CalenderDate.getTodaySql(1)));

        System.out.println("CUSTOMERS: TOTAL: " + newCustomersThisMonth);
        customerList.forEach(System.out::println);

        resultsMap.put("newCustomersThisMonth", newCustomersThisMonth);


        return ResponseEntity.ok(resultsMap);
    }

    public ResponseEntity<?> midBarGraphs() {
        Map<String, Object> resultsMap = new HashMap<>();

        List<LabelYPayload> totalDailyFormList = new ArrayList<>();
        List<LabelYPayload> completedDailyFormList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate currentDay = CalenderDate.getTodaySql(-i).toLocalDate();
            long totalDailyFormsCount = visitFormRepository.countByVisitAssignment_Date(Date.valueOf(currentDay));
            long completedDailyFormsCount = visitFormRepository.countByVisitAssignment_DateAndStatus(Date.valueOf(currentDay), VisitStatus.COMPLETED);

            // NO OF DAILY FORMS
            totalDailyFormList.add(new LabelYPayload(
                    currentDay.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    totalDailyFormsCount));

            // NO OF DAILY COMPLETED FORMS
            completedDailyFormList.add(new LabelYPayload(
                    currentDay.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()),
                    completedDailyFormsCount));

            System.out.println("TODAY IS: " + currentDay + " | DAY: " + currentDay.getDayOfWeek());
        }

        resultsMap.put("total", totalDailyFormList);
        resultsMap.put("completed", completedDailyFormList);

        return ResponseEntity.ok(resultsMap);
    }


}
