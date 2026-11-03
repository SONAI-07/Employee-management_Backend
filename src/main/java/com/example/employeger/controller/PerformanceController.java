package com.example.employeger.controller;

import com.example.employeger.model.PerformanceMetrics;
import com.example.employeger.repository.PerformanceRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceRepo performanceRepo;

    public PerformanceController(PerformanceRepo performanceRepo) {
        this.performanceRepo = performanceRepo;
    }

    // Employee views their own performance
    @GetMapping("/my-stats")
    public ResponseEntity<PerformanceMetrics> getMyStats() {
        String empId = SecurityContextHolder.getContext().getAuthentication().getName();
        PerformanceMetrics metrics = performanceRepo.findByEmployeeId(empId)
                .orElse(new PerformanceMetrics(empId));
        return ResponseEntity.ok(metrics);
    }
}