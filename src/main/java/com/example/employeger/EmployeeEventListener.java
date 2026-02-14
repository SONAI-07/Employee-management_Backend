package com.example.employeger;


import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;


  public class EmployeeEventListener
  {
    private emailservice emailService;

    @Async
    @EventListener
    public void EmployeeEvent (com.example.employeger.EmployeeEvent employeeEvent)

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
