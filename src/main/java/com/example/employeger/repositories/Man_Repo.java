package com.example.employeger.repository;

import com.example.employeger.model.Man_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Man_Repo extends JpaRepository<Man_model, String> {

    // Return Optional instead of raw object
    Optional<Man_model> findByManagerEmail(String managerEmail);
}