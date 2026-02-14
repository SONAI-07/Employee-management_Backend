package com.example.employeger;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Optional;


@Component
public class UserDetailServ implements UserDetailsService
{
    public Comp_Repo comp_Repo;
    private Man_model man_model;

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {

        UserDetails userdetails = User.builder().build();

        Optional<Emp_model> empModel = comp_Repo.findById(username);

        if (empModel.isPresent()) {

            String role_access = empModel.get().getEmp_id(); // Unwrap of Optional object

            if (role_access.charAt(0)=='E')
            {
                userdetails = User.builder()
                        .roles(empModel.get().getROLE())
                        .username(empModel.get().getEmp_name())
                        .password(empModel.get().emp_Password)
                        .build();
            }


                else if (role_access.charAt(0)=='M')
                {
                    userdetails = User.builder()
                        .roles(man_model.getROLE())
                        .username(man_model.getManager_name())
                        .password(man_model.getManager_Password())
                        .build();
                }


            return userdetails;
        }


        else
            throw new UsernameNotFoundException("Username not found");

    }

    }


















