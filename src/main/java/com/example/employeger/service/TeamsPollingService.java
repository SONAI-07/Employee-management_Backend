package com.example.employeger.service;

import com.example.employeger.model.ActivityEvent;
import com.example.employeger.model.Emp_model;
import com.example.employeger.repository.Comp_Repo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeamsPollingService {

    private final Comp_Repo empRepo;
    private final KafkaProducerService kafkaProducerService;

    public TeamsPollingService(Comp_Repo empRepo, KafkaProducerService kafkaProducerService) {
        this.empRepo = empRepo;
        this.kafkaProducerService = kafkaProducerService;
    }

    // Runs every 30 minutes
    @Scheduled(fixedRate = 1800000)
    public void pollTeamsActivity() {
        List<Emp_model> connectedEmployees = empRepo.findByTeamsConnectedTrue();

        for (Emp_model emp : connectedEmployees) {
            /*
             * PRODUCTION NOTE:
             * Here you would use emp.getMs_teams_access_token() to call:
             * GET https://graph.microsoft.com/v1.0/me/chats/getAllMessages
             * Then calculate the time difference between receiving a message and replying.
             */

            // For now, we push a heartbeat/activity event to keep the data pool flowing
            ActivityEvent event = new ActivityEvent(
                    emp.getEmp_id(),
                    "MS_TEAMS",
                    "TEAMS_ACTIVE_CHECK",
                    LocalDateTime.now()
            );

            kafkaProducerService.sendEvent(event);
        }
    }
}