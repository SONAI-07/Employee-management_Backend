package com.example.employeger.dto;

import java.time.LocalDateTime;

public record EmployeeStats(
        String id, String name, String email,
        int casualLeaves, int paidLeaves,
        String githubUsername, boolean githubConnected, boolean teamsConnected,
        int totalCommits, int totalPrMerged, int totalPrConflicts,
        double teamsResponsivenessScore, double performanceScore, double bonusAmount,
        LocalDateTime lastUpdated
) {}