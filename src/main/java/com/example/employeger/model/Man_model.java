package com.example.employeger;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Manager")
public class Man_model {

    private String Manager_name;
    private String Mang_Password;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Manager_id;

    private String Manager_email;
    private String ROLE;

    @OneToMany(mappedBy = "TeamManager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emp_model> employees = new ArrayList<>();

    public Man_model() {}

    // FIXED: Removed "M" concatenation and self-assignments
    public String getManager_name() { return Manager_name; }
    public void setManager_name(String Manager_name) { this.Manager_name = Manager_name; }

    public String getManager_id() { return Manager_id; }
    public void setManager_id(String Manager_id) { this.Manager_id = Manager_id; }

    public String getManager_Password() { return Mang_Password; }
    public void setManager_Password(String Mang_Password) { this.Mang_Password = Mang_Password; }

    public String getROLE() { return ROLE; }
    public void setROLE(String ROLE) { this.ROLE = ROLE; }

    public String getManager_email() { return Manager_email; }
    public void setManager_email(String Manager_email) { this.Manager_email = Manager_email; }

    public List<Emp_model> getEmployees() { return employees; }
    public void setEmployees(List<Emp_model> employees) { this.employees = employees; }
}