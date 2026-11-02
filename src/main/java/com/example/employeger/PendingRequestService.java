package com.example.employeger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PendingRequestService
{
    private final ApplicationEventPublisher publisher;
    private final BacklogRepo backRepo;

    @Autowired
    public PendingRequestService(ApplicationEventPublisher publisher, BacklogRepo backRepo) {
        this.publisher = publisher;
        this.backRepo = backRepo;
    }

    public String createPendingRequest(BacklogBase bactry)
    {
        if (bactry.getEmployee_id() == null || bactry.getEmployee_id().isBlank()) {
            bactry.setEmployee_id(UUID.randomUUID().toString());
        }

        backRepo.save(bactry);

        EmployeeEvent.EventType eventType = "LEAVE".equalsIgnoreCase(bactry.getType())
                ? EmployeeEvent.EventType.LEAVE_REQUEST
                : EmployeeEvent.EventType.SIGNUP;

        publisher.publishEvent(new EmployeeEvent(this, eventType, bactry));

        return "Employee registration details sent for Manager Verification.";
    }
}










