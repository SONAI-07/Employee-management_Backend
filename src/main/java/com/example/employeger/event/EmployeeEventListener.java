package com.example.employeger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmployeeEventListener
{
    private final emailservice emailService;

    @Autowired
    public EmployeeEventListener(emailservice emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void EmployeeEvent(EmployeeEvent employeeEvent)
    {
        switch (employeeEvent.getEventType()) {
            case SIGNUP:
                emailService.sendManagerVerificationEmail(employeeEvent.getBacklogBase());
                break;

            case LEAVE_REQUEST:
                emailService.sendEmployeeLeaveEmail(employeeEvent.getLeaveRequest());
                break;

            default:
                throw new IllegalArgumentException("Unsupported event type: " + employeeEvent.getEventType());
        }
    }
}
