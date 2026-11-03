package com.example.employeger;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableScheduling

public class EmployegerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployegerApplication.class, args);
    }

}
