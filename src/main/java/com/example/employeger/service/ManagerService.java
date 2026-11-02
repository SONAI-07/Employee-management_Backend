package com.example.employeger.service;

import com.example.employeger.model.BacklogBase;
import com.example.employeger.model.Emp_model;
import com.example.employeger.model.Man_model;
import com.example.employeger.repository.BacklogRepo;
import com.example.employeger.repository.Comp_Repo;
import com.example.employeger.repository.Man_Repo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagerService {

    private final BacklogRepo backlogRepo;
    private final Comp_Repo empRepo;
    private final Man_Repo manRepo;
    private final PasswordEncoder passwordEncoder;

    public ManagerService(BacklogRepo backlogRepo, Comp_Repo empRepo,
                          Man_Repo manRepo, PasswordEncoder passwordEncoder) {
        this.backlogRepo = backlogRepo;
        this.empRepo = empRepo;
        this.manRepo = manRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String approveRequest(String requestId) {
        BacklogBase request = backlogRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found or expired"));

        if ("SIGNUP".equals(request.getType())) {
            approveSignup(request);
        } else if ("LEAVE".equals(request.getType())) {
            approveLeave(request);
        } else {
            throw new RuntimeException("Unknown request type: " + request.getType());
        }

        backlogRepo.delete(request);
        return "Request approved successfully.";
    }

    @Transactional
    public String rejectRequest(String requestId) {
        BacklogBase request = backlogRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found or expired"));
        backlogRepo.delete(request);
        return "Request rejected and removed.";
    }

    private void approveSignup(BacklogBase request) {
        // Check if email already exists
        if (empRepo.existsByEmpEmail(request.getEmployeeEmail())) {
            throw new RuntimeException("Employee with this email already exists");
        }

        Emp_model emp = new Emp_model();
        emp.setEmp_name(request.getEmployeeName());
        emp.setEmp_Email(request.getEmployeeEmail());
        emp.setEmp_Password(passwordEncoder.encode(request.getPassword()));
        emp.setROLE("EMPLOYEE");
        emp.setCasual_leaves(12); // Default yearly quota
        emp.setPaid_leaves(15);   // Default yearly quota

        // Assign manager if specified
        if (request.getManager_name() != null) {
            manRepo.findByManagerEmail(request.getManager_name())
                   .ifPresent(emp::setTeamManager);
        }

        empRepo.save(emp);
    }

    private void approveLeave(BacklogBase request) {
        Emp_model emp = empRepo.findById(request.getEmployee_id())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (emp.getCasual_leaves() <= 0) {
            throw new RuntimeException("No casual leaves remaining");
        }

        emp.setCasual_leaves(emp.getCasual_leaves() - 1);
        empRepo.save(emp);
    }
}