package com.example.employeger;


import org.springframework.context.ApplicationEventPublisher;

public class PendingRequestService
{

    private ApplicationEventPublisher publisher;
    private BacklogRepo BackRepo;


    public String createPendingRequest(BacklogBase bactry)

    {
        bactry.setEmployeeEmail(bactry.getEmployeeEmail());
        bactry.setEmployeeName(bactry.getEmployeeName());
        bactry.setPassword(bactry.getPassword());
        bactry.setManager_name(bactry.getManager_name());

        BackRepo.save(bactry);

        publisher.publishEvent(new EmployeeEvent(this, EmployeeEvent.EventType.SIGNUP, null));

        return "Employee registration details sent for Manager Verification.";

    }
}










