package com.example.employeger.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ActivityEvent implements Serializable {
    private String employeeId;
    private String platform; // "GITHUB" or "MS_TEAMS"
    private String eventType; // "COMMIT", "PR_MERGED", "TEAMS_RESPONSE"
    private LocalDateTime timestamp;

    public ActivityEvent() {}

    public ActivityEvent(String employeeId, String platform, String eventType, LocalDateTime timestamp) {
        this.employeeId = employeeId;
        this.platform = platform;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}