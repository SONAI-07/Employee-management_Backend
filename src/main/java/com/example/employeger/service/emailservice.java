package com.example.employeger.service;

import com.example.employeger.model.BacklogBase;
import com.example.employeger.model.Man_model;
import com.example.employeger.repository.Man_Repo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class emailservice {

    private final JavaMailSender mailSender;
    private final Man_Repo manRepo;

    private static final String BASE_URL = "http://localhost:8080/api/manager";
    private static final String SYSTEM_EMAIL = "noreply@employeger.com";

    public emailservice(JavaMailSender mailSender, Man_Repo manRepo) {
        this.mailSender = mailSender;
        this.manRepo = manRepo;
    }

    public void sendManagerVerificationEmail(BacklogBase request) {
        String managerEmail = resolveManagerEmail(request.getManager_name());
        String approveLink = BASE_URL + "/approve/" + request.getEmployee_id();
        String rejectLink  = BASE_URL + "/reject/"  + request.getEmployee_id();

        String body = "Dear Manager,\n\n"
                + "A new employee (" + request.getEmployeeName() + ") has requested registration.\n\n"
                + "Approve: " + approveLink + "\n"
                + "Reject:  " + rejectLink + "\n\n"
                + "Regards,\nEmployeger Team";

        sendEmail(managerEmail, "Verify Employee Signup Request", body);
    }

    public void sendEmployeeLeaveEmail(BacklogBase request) {
        String managerEmail = resolveManagerEmail(request.getManager_name());
        String approveLink = BASE_URL + "/approve/" + request.getEmployee_id();
        String rejectLink  = BASE_URL + "/reject/"  + request.getEmployee_id();

        String body = "Dear Manager,\n\n"
                + "Employee " + request.getEmployeeName() + " has requested leave.\n"
                + "Reason: " + (request.getLeaveReason() != null ? request.getLeaveReason() : "N/A") + "\n\n"
                + "Approve: " + approveLink + "\n"
                + "Reject:  " + rejectLink + "\n\n"
                + "Regards,\nEmployeger Team";

        sendEmail(managerEmail, "Employee Leave Request", body);
    }

    private String resolveManagerEmail(String managerIdentifier) {
        if (managerIdentifier == null || managerIdentifier.isBlank()) {
            return "manager@example.com";
        }
        return manRepo.findByManagerEmail(managerIdentifier)
                .map(Man_model::getManager_email)
                .orElse(managerIdentifier);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SYSTEM_EMAIL);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}