package com.example.demo.controllers;

import com.example.demo.dto.DashboardDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.dashboard.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getAtelierStats() {
        DashboardDto stats = dashboardService.getAtelierStats();

        ApiResponse response = new ApiResponse(
                "Dashboard stats fetched successfully",
                stats,
                true
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
