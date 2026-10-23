package com.example.employeger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class emailservice
{
    private final JavaMailSender mailSender;
    private final Man_Repo manRepo;

    @Autowired
    public emailservice(JavaMailSender mailSender, Man_Repo manRepo) {
        this.mailSender = mailSender;
        this.manRepo = manRepo;
    }

    public void sendManagerVerificationEmail(BacklogBase backmodel)
    {
        String managerEmail = null;
        if (backmodel.getManager_name() != null) {
            managerEmail = manRepo.findByManagerEmail(backmodel.getManager_name())
                    .map(Man_model::getManager_email)
                    .orElse(backmodel.getManager_name());
        }
        if (managerEmail == null) {
            managerEmail = "manager@example.com";
        }

        String subject = "Verify Employee Signup Request";
        String verificationLink = "http://localhost:8080/api/manager/verify?empId=" + backmodel.getEmployee_id();

        String body = "Dear Manager,\n\n"
                + "A new employee (" + backmodel.getEmployeeName() + ") has requested for Company Profile registration under your Team.\n\n"
                + "Approve: " + verificationLink + "\n"
                + "Regards,\nEmployeger Team";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(backmodel.getEmployeeEmail() != null ? backmodel.getEmployeeEmail() : "noreply@employeger.com");
        message.setTo(managerEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendEmployeeLeaveEmail(BacklogBase leaveRequest)
    {
        String managerEmail = null;
        if (leaveRequest.getManager_name() != null) {
            managerEmail = manRepo.findByManagerEmail(leaveRequest.getManager_name())
                    .map(Man_model::getManager_email)
                    .orElse(leaveRequest.getManager_name());
        }
        if (managerEmail == null) {
            managerEmail = "manager@example.com";
        }

        String subject = "Verify Employee Leave Request";
        String Leave_updation = "http://localhost:8080/api/manager/leave?empId=" + leaveRequest.getEmployee_id();

        String body = "Dear Manager,\n\n"
                + "Employee " + leaveRequest.getEmployeeName() + " has requested for Leaves under Employee Welfare Policy.\n"
                + "Reason: " + (leaveRequest.getLeaveReason() != null ? leaveRequest.getLeaveReason() : "N/A") + "\n"
                + "Visit the updation link for the Leave Approval/Rejection.\n"
                + Leave_updation + "\n\n"
                + "Regards,\nEmployeger Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(leaveRequest.getEmployeeEmail() != null ? leaveRequest.getEmployeeEmail() : "noreply@employeger.com");
        message.setTo(managerEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendEmployeeLeaveEmail(@org.jetbrains.annotations.NotNull Emp_model empModel)
    {
        String managerEmail = empModel.getTeamManager() != null ? empModel.getTeamManager().getManager_email() : "manager@example.com";

        String subject = "Verify Employee Leave Request";
        String Leave_updation = "http://localhost:8080/api/manager/leave?empId=" + empModel.getEmp_id();

        String body = "Dear Manager,\n\n"
                + "Employee " + empModel.getEmp_name() + " has requested for Leaves under Employee Welfare Policy.\n"
                + "Visit the updation link for the Leave Approval/Rejection.\n"
                + Leave_updation + "\n\n"
                + "Regards,\nEmployeger Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(empModel.getEmp_Email());
        message.setTo(managerEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}


