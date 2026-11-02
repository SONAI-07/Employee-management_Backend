package com.example.employeger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailServ implements UserDetailsService
{
    private final Comp_Repo comp_Repo;
    private final Man_Repo man_Repo;

    @Autowired
    public UserDetailServ(Comp_Repo comp_Repo, Man_Repo man_Repo) {
        this.comp_Repo = comp_Repo;
        this.man_Repo = man_Repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Check if employee by ID or Email
        Optional<Emp_model> empModel = comp_Repo.findById(username);
        if (empModel.isEmpty()) {
            empModel = comp_Repo.findByEmpEmail(username);
        }

        if (empModel.isPresent()) {
            Emp_model emp = empModel.get();
            String role = emp.getROLE() != null ? emp.getROLE().replace("ROLE_", "") : "EMPLOYEE";
            return User.builder()
                    .username(emp.getEmp_name() != null ? emp.getEmp_name() : emp.getEmp_id())
                    .password(emp.getEmp_Password())
                    .roles(role)
                    .build();
        }

        // Check if manager by ID or Email
        Optional<Man_model> manModel = man_Repo.findById(username);
        if (manModel.isEmpty()) {
            manModel = man_Repo.findByManagerEmail(username);
        }

        if (manModel.isPresent()) {
            Man_model man = manModel.get();
            String role = man.getROLE() != null ? man.getROLE().replace("ROLE_", "") : "MANAGER";
            return User.builder()
                    .username(man.getManager_name() != null ? man.getManager_name() : man.getManager_id())
                    .password(man.getManager_Password())
                    .roles(role)
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username/id: " + username);
    }
}


















