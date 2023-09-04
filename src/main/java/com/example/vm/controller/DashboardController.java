package com.example.vm.controller;

import com.example.vm.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping("/dash")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/count")
    public ResponseEntity<?> topBarCounts(){
        return dashboardService.topBarCounts();
    }

    @GetMapping("/graph")
    public ResponseEntity<?> midBarGraphs(){
        return dashboardService.midBarGraphs();
    }
}
