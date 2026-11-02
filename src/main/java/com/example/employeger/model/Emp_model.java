package com.example.employeger;

import jakarta.persistence.*;

@Entity
@Table(name = "Employee")
public class Emp_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String emp_id;

    private String emp_name;
    private String emp_Password;
    private String emp_Email;
    private int emp_Salary;
    private String ROLE;
    private int Casual_leaves;
    private int paid_leaves;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Manager_id")
    private Man_model TeamManager;

    public Emp_model() {}

    // FIXED: Removed "E" concatenation and self-assignments
    public String getEmp_id() { return emp_id; }
    public void setEmp_id(String emp_id) { this.emp_id = emp_id; }

    public String getEmp_name() { return emp_name; }
    public void setEmp_name(String emp_name) { this.emp_name = emp_name; }

    public String getEmp_Password() { return emp_Password; }
    public void setEmp_Password(String emp_Password) { this.emp_Password = emp_Password; }

    public String getEmp_Email() { return emp_Email; }
    public void setEmp_Email(String emp_Email) { this.emp_Email = emp_Email; }

    public int getEmp_Salary() { return emp_Salary; }
    public void setEmp_Salary(int emp_Salary) { this.emp_Salary = emp_Salary; }

    public String getROLE() { return ROLE; }
    public void setROLE(String ROLE) { this.ROLE = ROLE; }

    public Man_model getTeamManager() { return TeamManager; }
    public void setTeamManager(Man_model teamManager) { this.TeamManager = teamManager; }

    public int getCasual_leaves() { return Casual_leaves; }
    public void setCasual_leaves(int casual_leaves) { this.Casual_leaves = casual_leaves; }

    public int getPaid_leaves() { return paid_leaves; }
    public void setPaid_leaves(int paid_leaves) { this.paid_leaves = paid_leaves; }
}