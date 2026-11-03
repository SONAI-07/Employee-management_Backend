package com.example.employeger.controller;

import com.example.employeger.model.ActivityEvent;
import com.example.employeger.model.Emp_model;
import com.example.employeger.repository.Comp_Repo;
import com.example.employeger.service.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final Comp_Repo empRepo;
    private final KafkaProducerService kafkaProducerService;

    public WebhookController(Comp_Repo empRepo, KafkaProducerService kafkaProducerService) {
        this.empRepo = empRepo;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/github")
    public ResponseEntity<String> handleGitHubWebhook(
            @RequestHeader("X-GitHub-Event") String eventType,
            @RequestBody Map<String, Object> payload) {

        try {
            Map<String, Object> sender = (Map<String, Object>) payload.get("sender");
            if (sender == null) return ResponseEntity.ok("No sender found");

            String githubUsername = (String) sender.get("login");
            Emp_model emp = empRepo.findByGithubUsername(githubUsername).orElse(null);

            if (emp == null) return ResponseEntity.ok("User not registered in portal");

            ActivityEvent event = new ActivityEvent();
            event.setEmployeeId(emp.getEmp_id());
            event.setPlatform("GITHUB");
            event.setTimestamp(LocalDateTime.now());

            // 1. Capture Commits
            if ("push".equals(eventType)) {
                event.setEventType("COMMIT");
                kafkaProducerService.sendEvent(event);
            }
            // 2. Capture PR Merges / Conflicts
            else if ("pull_request".equals(eventType)) {
                String action = (String) payload.get("action");
                if ("closed".equals(action)) {
                    Map<String, Object> pr = (Map<String, Object>) payload.get("pull_request");
                    boolean merged = (boolean) pr.get("merged");
                    event.setEventType(merged ? "PR_MERGED" : "PR_CLOSED_UNMERGED");
                    kafkaProducerService.sendEvent(event);
                }
            }

            return ResponseEntity.ok("Event captured");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing webhook: " + e.getMessage());
        }
    }
}