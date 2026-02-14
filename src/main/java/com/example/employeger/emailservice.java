package com.example.employeger;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class emailservice
{
    private JavaMailSender mailSender;

    private BacklogBase backlogBase;
    private Man_model man_model;

    public void sendManagerVerificationEmail (BacklogBase backmodel)
    {

        String managerEmail = man_model.getManager_email();

        String subject = "Verify Employee Signup Request";
        String verificationLink = "http://localhost:8080/api/manager/verify?empId=" + backlogBase.getEmployee_id();

        String body = "Dear Manager,\n\n"
                + "A new employee (" + backmodel.getEmployeeName() + ") has requested for Company Profile registration under your Team.\n\n"
                + "Approve: " + verificationLink + "\n"
                + "Regards,\nEmployeger Team";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(backlogBase.getEmployeeEmail());
        message.setTo(managerEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }





    public void sendEmployeeLeaveEmail(@org.jetbrains.annotations.NotNull Emp_model empModel)
    {

        String managerEmail = empModel.getTeamManager().getManager_email();

        String subject = "Verify Employee Leave Request";
        String  Leave_updation = "http://localhost:8080/api/manager/leave?empId=" + empModel.getEmp_id();

        String body = "Dear Manager,\n\n"
                       + "Employee "+ empModel.getEmp_name()+ "has requested for Leaves under Employee Welfare Policy"
                       + "Visit the updation link for the Leave Approval/Rejection.\n"
                        + Leave_updation + "\n\n"
                        +"Regards\n Employeger Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(empModel.emp_Email);
        message.setTo(managerEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

    }






}


