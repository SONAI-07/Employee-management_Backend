package com.example.employeger;

import com.example.employeger.model.Emp_model;
import com.example.employeger.model.Man_model;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServ implements UserDetailsService {

    private final Comp_Repo compRepo;
    private final Man_Repo manRepo;

    // Constructor Injection
    public UserDetailServ(Comp_Repo compRepo, Man_Repo manRepo) {
        this.compRepo = compRepo;
        this.manRepo = manRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find as Employee first
        Optional<Emp_model> empModel = compRepo.findById(username);
        if (empModel.isPresent()) {
            Emp_model emp = empModel.get();
            return User.builder()
                    .username(emp.getEmp_name())
                    .password(emp.getEmp_Password())
                    .roles(emp.getROLE()) // Spring Security automatically adds "ROLE_" prefix
                    .build();
        }

        // Try to find as Manager
        Optional<Man_model> manModel = manRepo.findById(username);
        if (manModel.isPresent()) {
            Man_model man = manModel.get();
            return User.builder()
                    .username(man.getManager_name())
                    .password(man.getManager_Password())
                    .roles(man.getROLE())
                    .build();
        }

        throw new UsernameNotFoundException("User not found with ID: " + username);
    }
}