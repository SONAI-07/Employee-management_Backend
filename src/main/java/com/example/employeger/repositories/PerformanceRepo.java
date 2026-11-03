package com.example.employeger.repository;

import com.example.employeger.model.PerformanceMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerformanceRepo extends JpaRepository<PerformanceMetrics, Long> {
    Optional<PerformanceMetrics> findByEmployeeId(String employeeId);
}