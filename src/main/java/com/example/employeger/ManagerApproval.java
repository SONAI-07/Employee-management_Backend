package com.example.employeger;


 import jakarta.transaction.Transactional;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 import org.springframework.stereotype.Service;


  @Service
   public class ManagerApproval {

      private final Comp_Repo empRepository;
      private final Man_Repo managerRepository;


      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();



      @Autowired
      public ManagerApproval(Comp_Repo empRepository,
                             Man_Repo managerRepository)
      {
          this.empRepository = empRepository;
          this.managerRepository = managerRepository;
      }




      @Transactional
      public void promoteSignup(BacklogBase backlog)

      {

          if (!"SIGNUP".equalsIgnoreCase(backlog.getType())) {
              throw new IllegalArgumentException("Invalid request type for signup promotion");
          }

          // Convert BacklogBase → Emp_model

          Emp_model emp = new Emp_model();

          emp.setEmp_name(backlog.getEmployeeName());
          emp.setEmp_Email(backlog.getEmployeeEmail());

          String hashedPassword = encoder.encode(backlog.getPassword());
          emp.setEmp_Password(hashedPassword);

          emp.setROLE("EMPLOYEE");
          emp.setCasual_leaves(10);
          emp.setEmp_Salary(35000);
          emp.setPaid_leaves(25);

          // Link manager using email stored in backlog
          Man_model manager = managerRepository.findByManagerEmail(backlog.getManagerName())
                  .orElseThrow(() -> new IllegalStateException("Manager does not exist!"));

          emp.setTeamManager(manager);

          // Save to PostgreSQL
          empRepository.save(emp);


      }

  }






