package com.example.employeger;

import com.example.employeger.model.BacklogBase; // Update import if you moved this to model/
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PendingRequestService {

    private final ApplicationEventPublisher publisher;
    private final BacklogRepo backRepo;

    // Constructor Injection
    public PendingRequestService(ApplicationEventPublisher publisher, BacklogRepo backRepo) {
        this.publisher = publisher;
        this.backRepo = backRepo;
    }

    public String createPendingRequest(BacklogBase request) {
        // 1. Save to Redis
        backRepo.save(request);

        // 2. Dynamically determine event type based on the request
        EmployeeEvent.EventType eventType = "LEAVE".equals(request.getType())
                ? EmployeeEvent.EventType.LEAVE_REQUEST
                : EmployeeEvent.EventType.SIGNUP;

        // 3. Publish the event with the ACTUAL saved object (Fixed the NPE)
        publisher.publishEvent(new EmployeeEvent(this, eventType, request));

        return "Request submitted and pending manager approval.";
    }
}