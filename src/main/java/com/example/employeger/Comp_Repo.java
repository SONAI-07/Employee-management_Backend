package com.example.employeger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Comp_Repo extends JpaRepository < Emp_model , String > {
    boolean existsByEmpEmail(String empEmail);
}
