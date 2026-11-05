package com.example.employeger.service;

import com.example.employeger.dto.EmployeeStats;
import com.example.employeger.dto.EmployeeSummary;
import com.example.employeger.model.BacklogBase;
import com.example.employeger.model.Emp_model;
import com.example.employeger.model.PerformanceMetrics;
import com.example.employeger.repositories.BacklogRepo;
import com.example.employeger.repositories.Comp_Repo;
import com.example.employeger.repositories.Man_Repo;
import com.example.employeger.repositories.PerformanceRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerService {

    private final BacklogRepo backlogRepo;
    private final Comp_Repo empRepo;
    private final Man_Repo manRepo;
    private final PerformanceRepo performanceRepo;
    private final PasswordEncoder passwordEncoder;

    public ManagerService(BacklogRepo backlogRepo, Comp_Repo empRepo, Man_Repo manRepo,
                          PerformanceRepo performanceRepo, PasswordEncoder passwordEncoder) {
        this.backlogRepo = backlogRepo;
        this.empRepo = empRepo;
        this.manRepo = manRepo;
        this.performanceRepo = performanceRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /* ---------- NEW: pending requests from Redis backlog ---------- */
    public List<BacklogBase> getPendingRequests() {
        List<BacklogBase> pending = new ArrayList<>();
        backlogRepo.findAll().forEach(pending::add);
        return pending;
    }

    /* ---------- NEW: team directory (name boxes) ---------- */
    public List<EmployeeSummary> getTeamSummary() {
        return empRepo.findAll().stream().map(this::toSummary).toList();
    }

    /* ---------- NEW: full stats for one team member ---------- */
    public EmployeeStats getTeamMemberStats(String empId) {
        Emp_model emp = empRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + empId));
        PerformanceMetrics m = performanceRepo.findByEmployeeId(empId).orElse(null);

        return new EmployeeStats(
                emp.getEmp_id(), emp.getEmp_name(), emp.getEmp_Email(),
                emp.getCasual_leaves(), emp.getPaid_leaves(),
                emp.getGithub_username(), emp.isGithub_connected(), emp.isTeams_connected(),
                m != null ? m.getTotalCommits() : 0,
                m != null ? m.getTotalPrMerged() : 0,
                m != null ? m.getTotalPrConflicts() : 0,
                m != null ? m.getTeamsResponsivenessScore() : 0,
                m != null ? m.getPerformanceScore() : 0,
                m != null ? m.getBonusAmount() : 0,
                m != null ? m.getLastUpdated() : null);
    }

    private EmployeeSummary toSummary(Emp_model emp) {
        return new EmployeeSummary(emp.getEmp_id(), emp.getEmp_name(), emp.getEmp_Email(),
                emp.getCasual_leaves(), emp.getPaid_leaves(),
                emp.getGithub_username(), emp.isGithub_connected(), emp.isTeams_connected());
    }

    /* ---------- Approvals (unchanged) ---------- */
    @Transactional
    public String approveRequest(String requestId) {
        BacklogBase request = backlogRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found or expired"));
        if ("SIGNUP".equals(request.getType())) approveSignup(request);
        else if ("LEAVE".equals(request.getType())) approveLeave(request);
        else throw new RuntimeException("Unknown request type: " + request.getType());
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
        if (empRepo.existsByEmpEmail(request.getEmployeeEmail()))
            throw new RuntimeException("Employee with this email already exists");
        Emp_model emp = new Emp_model();
        emp.setEmp_name(request.getEmployeeName());
        emp.setEmp_Email(request.getEmployeeEmail());
        emp.setEmp_Password(passwordEncoder.encode(request.getPassword()));
        emp.setROLE("EMPLOYEE");
        emp.setCasual_leaves(12);
        emp.setPaid_leaves(15);
        if (request.getManager_name() != null)
            manRepo.findByManagerEmail(request.getManager_name()).ifPresent(emp::setTeamManager);
        empRepo.save(emp);
    }

    private void approveLeave(BacklogBase request) {
        Emp_model emp = empRepo.findById(request.getEmployee_id())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        if (emp.getCasual_leaves() <= 0) throw new RuntimeException("No casual leaves remaining");
        emp.setCasual_leaves(emp.getCasual_leaves() - 1);
        empRepo.save(emp);
    }
}