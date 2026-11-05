package com.example.employeger.controller;

import com.example.employeger.dto.EmployeeStats;
import com.example.employeger.dto.EmployeeSummary;
import com.example.employeger.model.BacklogBase;
import com.example.employeger.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<BacklogBase>> getPendingRequests() {
        return ResponseEntity.ok(managerService.getPendingRequests());
    }

    @GetMapping("/team")
    public ResponseEntity<List<EmployeeSummary>> getTeam() {
        return ResponseEntity.ok(managerService.getTeamSummary());
    }

    @GetMapping("/team/{empId}/stats")
    public ResponseEntity<EmployeeStats> getTeamMemberStats(@PathVariable String empId) {
        return ResponseEntity.ok(managerService.getTeamMemberStats(empId));
    }

    @PostMapping("/approve/{requestId}")
    public ResponseEntity<String> approve(@PathVariable String requestId) {
        return ResponseEntity.ok(managerService.approveRequest(requestId));
    }

    @PostMapping("/reject/{requestId}")
    public ResponseEntity<String> reject(@PathVariable String requestId) {
        return ResponseEntity.ok(managerService.rejectRequest(requestId));
    }
}