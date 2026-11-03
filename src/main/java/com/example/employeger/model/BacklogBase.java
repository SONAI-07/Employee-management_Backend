package com.example.employeger.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@RedisHash("PendingRequest")
public class BacklogBase implements Serializable {

    @Id
    private String Employee_id;

    @TimeToLive
    private Long expiration = 86400L; // 24 hours

    private String type;
    private String employeeName;
    private String employeeEmail;
    private String password;
    private String Manager_name;
    private String leaveReason;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String Role;

    // FIXED: Auto-generate UUID if not provided
    public BacklogBase() {
        if (this.Employee_id == null) {
            this.Employee_id = UUID.randomUUID().toString();
        }
    }

    // Getters and Setters
    public String getEmployee_id() { return Employee_id; }
    public void setEmployee_id(String employee_id) { this.Employee_id = employee_id; }

    public String getRole() { return Role; }
    public void setRole(String Role) { this.Role = Role; }

    public String getManager_name() { return Manager_name; }
    public void setManager_name(String Manager_name) { this.Manager_name = Manager_name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmployeeEmail() { return employeeEmail; }
    public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLeaveReason() { return leaveReason; }
    public void setLeaveReason(String leaveReason) { this.leaveReason = leaveReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}