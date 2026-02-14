package com.example.employeger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Employee-Services")


public class EmployeeController
{


 private PendingRequestService pendingRequestService;


    @PostMapping("/leave")
    public ResponseEntity<String> submitLeave(@RequestBody BacklogBase leaveRequest)
    {
        leaveRequest.setType("LEAVE");
        pendingRequestService.createPendingRequest(leaveRequest);
        return ResponseEntity.ok("Leave request submitted and pending approval.");
    }






    @PostMapping("/signup")
    public ResponseEntity<String> submitSignup(@RequestBody BacklogBase signupEntry)
    {
        signupEntry.setType("SIGNUP");
        pendingRequestService.createPendingRequest(signupEntry);
        return ResponseEntity.ok("Signup request submitted and pending approval.");

    }


}










