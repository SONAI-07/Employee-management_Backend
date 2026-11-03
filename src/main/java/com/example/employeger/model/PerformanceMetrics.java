package com.example.employeger.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "performance_metrics")
public class PerformanceMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;
    private int totalCommits;
    private int totalPrMerged;
    private int totalPrConflicts;
    private double teamsResponsivenessScore;
    private double performanceScore;
    private double bonusAmount;
    private LocalDateTime lastUpdated;

    public PerformanceMetrics() {}

    public PerformanceMetrics(String employeeId) {
        this.employeeId = employeeId;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public int getTotalCommits() { return totalCommits; }
    public void setTotalCommits(int totalCommits) { this.totalCommits = totalCommits; }

    public int getTotalPrMerged() { return totalPrMerged; }
    public void setTotalPrMerged(int totalPrMerged) { this.totalPrMerged = totalPrMerged; }

    public int getTotalPrConflicts() { return totalPrConflicts; }
    public void setTotalPrConflicts(int totalPrConflicts) { this.totalPrConflicts = totalPrConflicts; }

    public double getTeamsResponsivenessScore() { return teamsResponsivenessScore; }
    public void setTeamsResponsivenessScore(double score) { this.teamsResponsivenessScore = score; }

    public double getPerformanceScore() { return performanceScore; }
    public void setPerformanceScore(double performanceScore) { this.performanceScore = performanceScore; }

    public double getBonusAmount() { return bonusAmount; }
    public void setBonusAmount(double bonusAmount) { this.bonusAmount = bonusAmount; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}