package com.example.employeger.controller;

import com.example.employeger.model.BacklogBase;
import com.example.employeger.PendingRequestService; // Change to .service.PendingRequestService if moved
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee") // Now perfectly matches SecurityConfig
public class EmployeeController {

    private final PendingRequestService pendingRequestService;

    public EmployeeController(PendingRequestService pendingRequestService) {
        this.pendingRequestService = pendingRequestService;
    }

    @PostMapping("/leave")
    public ResponseEntity<String> submitLeave(@RequestBody BacklogBase leaveRequest) {
        leaveRequest.setType("LEAVE");
        pendingRequestService.createPendingRequest(leaveRequest);
        return ResponseEntity.ok("Leave request submitted and pending approval.");
    }
}