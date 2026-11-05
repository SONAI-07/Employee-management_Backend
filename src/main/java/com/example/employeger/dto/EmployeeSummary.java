package com.example.employeger.dto;

public record EmployeeSummary(
        String id, String name, String email,
        int casualLeaves, int paidLeaves,
        String githubUsername, boolean githubConnected, boolean teamsConnected
) {}