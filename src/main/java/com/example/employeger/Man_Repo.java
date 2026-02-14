package com.example.employeger;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
 public interface Man_Repo extends JpaRepository <Man_model, String>
 {
  public Man_model findByManagerEmail(String managerEmail);
 }

