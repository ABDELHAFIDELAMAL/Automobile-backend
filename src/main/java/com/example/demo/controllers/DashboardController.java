package com.example.demo.controllers;

import com.example.demo.dto.DashboardDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.services.dashboard.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse> getAtelierStats() {
        try {
            DashboardDto data = dashboardService.getAtelierStats();
            return ResponseEntity.ok(new ApiResponse("Dashboard stats fetched successfully", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to fetch dashboard stats", null, false));
        }
    }
}
