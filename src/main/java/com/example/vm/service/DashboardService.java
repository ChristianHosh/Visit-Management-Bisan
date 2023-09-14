package com.example.vm.service;

import com.example.vm.model.Location;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.payload.report.LabelYPayload;
import com.example.vm.repository.CustomerRepository;
import com.example.vm.repository.LocationRepository;
import com.example.vm.repository.PaymentReceiptRepository;
import com.example.vm.repository.VisitFormRepository;
import com.example.vm.service.util.CalenderDate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class DashboardService {

    private final VisitFormRepository visitFormRepository;
    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;
    private final PaymentReceiptRepository paymentReceiptRepository;

    public DashboardService(VisitFormRepository visitFormRepository,
                            CustomerRepository customerRepository,
                            LocationRepository locationRepository,
                            PaymentReceiptRepository paymentReceiptRepository) {
        this.visitFormRepository = visitFormRepository;
        this.customerRepository = customerRepository;
        this.locationRepository = locationRepository;
        this.paymentReceiptRepository = paymentReceiptRepository;
    }

    public ResponseEntity<?> topBarCounts() {
        Map<String, Object> resultsMap = new HashMap<>();

        // TODAY'S NUMBER OF COMPLETED FORMS / TOTAL FORMS
        long totalFormsForToday = visitFormRepository.countByVisitAssignment_Date(CalenderDate.getTodaySql());
        long completedFormsForToday = visitFormRepository.countByVisitAssignment_DateAndStatus(CalenderDate.getTodaySql(), VisitStatus.COMPLETED);

        resultsMap.put("totalToday", totalFormsForToday);
        resultsMap.put("completedToday", completedFormsForToday);

        // THIS WEEK'S NUMBER OF CANCELED FORMS
        long canceledFormsForThisWeek = visitFormRepository.countByVisitAssignment_DateBetweenAndStatus(CalenderDate.getTodaySql(-7), CalenderDate.getTodaySql(1), VisitStatus.CANCELED);
        resultsMap.put("canceledFormsThisWeek", canceledFormsForThisWeek);

        // THIS MONTH'S CUSTOMER GROWTH NUMBERS / INCREASE % SINCE LAST MONTH
        long newCustomersThisMonth = customerRepository.countByCreatedTimeBetween(CalenderDate.asTimestamp(CalenderDate.getTodaySql(-30)), CalenderDate.asTimestamp(CalenderDate.getTodaySql(1)));
        resultsMap.put("newCustomersThisMonth", newCustomersThisMonth);

        // CURRENTLY UNDERGOING
        double revenue = paymentReceiptRepository.countAmountByCreatedAfter(CalenderDate.getTodaySql(-14));
        resultsMap.put("revenue", revenue);

        return ResponseEntity.ok(resultsMap);
    }

    public ResponseEntity<?> midBarGraphs() {
        Map<String, Object> resultsMap = new HashMap<>();

        List<LabelYPayload> totalDailyFormList = new ArrayList<>();
        List<LabelYPayload> completedDailyFormList = new ArrayList<>();
        List<LabelYPayload> popularLocationList = new ArrayList<>();

        // TOP 5 MOST POPULAR LOCATIONS
        List<Location> locationList = locationRepository.findAll();
        for (Location location: locationList){
            long formsInLocation = visitFormRepository.countByVisitAssignment_VisitDefinition_LocationAndVisitAssignment_DateBetweenAndVisitAssignment_EnabledTrueAndEnabledTrue(location, CalenderDate.getTodaySql(-14), CalenderDate.getTodaySql(1));

            popularLocationList.add(new LabelYPayload(
                    location.getAddress(),
                    formsInLocation
            ));
        }
        popularLocationList.sort(Comparator.comparing(LabelYPayload::getY));
        Collections.reverse(popularLocationList);

        popularLocationList = popularLocationList.subList(0,5);



        // NO OF PREVIOUS DAILY COMPLETED FORMS
        for (int i = 0; i < 7; i++) {
            LocalDate currentDay = CalenderDate.getTodaySql(-i+1).toLocalDate();
            long completedDailyFormsCount = visitFormRepository.countByVisitAssignment_DateAndStatus(Date.valueOf(currentDay), VisitStatus.COMPLETED);

            completedDailyFormList.add(new LabelYPayload(
                    currentDay.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    completedDailyFormsCount));
        }

        // NO OF NEXT MONTH TOTAL
        for (int i = 0; i < 30; i++) {
            LocalDate currentDay = CalenderDate.getTodaySql(i-3).toLocalDate();
            long totalDailyFormsCount = visitFormRepository.countByVisitAssignment_Date(Date.valueOf(currentDay));

            totalDailyFormList.add(new LabelYPayload(
                    currentDay.format(DateTimeFormatter.ISO_DATE),
                    totalDailyFormsCount));
        }
        Collections.reverse(completedDailyFormList);

        // PUTTING ARRAYS OF DATA POINT
        resultsMap.put("total", totalDailyFormList);
        resultsMap.put("completed", completedDailyFormList);
        resultsMap.put("locations", popularLocationList);

        return ResponseEntity.ok(resultsMap);
    }


}
