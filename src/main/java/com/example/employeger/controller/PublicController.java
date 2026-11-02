package com.example.employeger.controller;

import com.example.employeger.model.BacklogBase;
import com.example.employeger.PendingRequestService; // Change to .service.PendingRequestService if moved
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final PendingRequestService pendingRequestService;

    public PublicController(PendingRequestService pendingRequestService) {
        this.pendingRequestService = pendingRequestService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> submitSignup(@RequestBody BacklogBase signupEntry) {
        signupEntry.setType("SIGNUP");
        pendingRequestService.createPendingRequest(signupEntry);
        return ResponseEntity.ok("Signup request submitted and pending approval.");
    }
}