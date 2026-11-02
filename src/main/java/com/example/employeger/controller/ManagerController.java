package com.example.employeger.controller;

import com.example.employeger.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
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