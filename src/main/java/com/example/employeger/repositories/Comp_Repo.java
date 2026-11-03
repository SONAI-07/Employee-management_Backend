package com.example.employeger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Comp_Repo extends JpaRepository<Emp_model, String> {
    @Query("SELECT COUNT(e) > 0 FROM Emp_model e WHERE e.emp_Email = :empEmail")
    boolean existsByEmpEmail(@Param("empEmail") String empEmail);

    @Query("SELECT e FROM Emp_model e WHERE e.emp_Email = :empEmail")
    Optional<Emp_model> findByEmpEmail(@Param("empEmail") String empEmail);

    Optional<Emp_model> findByGithubUsername(String githubUsername);

    List<Emp_model> findByTeamsConnectedTrue();

}
