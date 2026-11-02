package com.example.employeger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Man_Repo extends JpaRepository<Man_model, String> {
    @Query("SELECT m FROM Man_model m WHERE m.Manager_email = :managerEmail")
    Optional<Man_model> findByManagerEmail(@Param("managerEmail") String managerEmail);
}

