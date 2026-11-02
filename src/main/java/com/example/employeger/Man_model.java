package com.example.employeger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Manager")
public class Man_model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Manager_id;

    private String Manager_name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String Mang_Password;

    private String Manager_email;
    private String ROLE;

    @OneToMany(mappedBy = "TeamManager", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("TeamManager")
    private List<Emp_model> employees = new ArrayList<>();

    // Getters and Setters

    public String getManager_name() {
        return Manager_name;
    }

    public void setManager_name(String manager_name) {
        this.Manager_name = manager_name;
    }

    public String getManager_id() {
        return Manager_id;
    }

    public void setManager_id(String manager_id) {
        this.Manager_id = manager_id;
    }

    public String getManager_Password() {
        return Mang_Password;
    }

    public void setManager_Password(String mang_Password) {
        this.Mang_Password = mang_Password;
    }

    public String getROLE() {
        return ROLE;
    }

    public void setROLE(String role) {
        this.ROLE = role;
    }

    public String getManager_email() {
        return Manager_email;
    }

    public void setManager_email(String manager_email) {
        this.Manager_email = manager_email;
    }

    public List<Emp_model> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Emp_model> employees) {
        this.employees = employees;
    }
}
