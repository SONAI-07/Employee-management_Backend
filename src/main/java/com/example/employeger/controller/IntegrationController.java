package com.example.employeger.controller;

import com.example.employeger.service.IntegrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

    private final IntegrationService integrationService;

    @Value("${github.client-id}")
    private String githubClientId;
    @Value("${github.redirect-uri}")
    private String githubRedirectUri;

    @Value("${microsoft.client-id}")
    private String msClientId;
    @Value("${microsoft.redirect-uri}")
    private String msRedirectUri;

    public IntegrationController(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    // Frontend calls this to get the GitHub Login URL
    @GetMapping("/github/initiate")
    public ResponseEntity<String> initiateGitHub() {
        // Gets the currently logged-in Employee ID
        String empId = SecurityContextHolder.getContext().getAuthentication().getName();

        String authUrl = "https://github.com/login/oauth/authorize?client_id=" + githubClientId +
                "&redirect_uri=" + URLEncoder.encode(githubRedirectUri, StandardCharsets.UTF_8) +
                "&scope=repo&state=" + empId;

        return ResponseEntity.ok(authUrl);
    }

    // GitHub redirects here after user approves
    @GetMapping("/github/callback")
    public ResponseEntity<String> githubCallback(@RequestParam String code, @RequestParam String state) {
        integrationService.processGitHubCallback(state, code);
        return ResponseEntity.ok("<html><body><h3>GitHub Connected Successfully! You can close this tab.</h3></body></html>");
    }

    // Frontend calls this to get the Microsoft Login URL
    @GetMapping("/microsoft/initiate")
    public ResponseEntity<String> initiateMicrosoft() {
        String empId = SecurityContextHolder.getContext().getAuthentication().getName();

        String authUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=" + msClientId +
                "&redirect_uri=" + URLEncoder.encode(msRedirectUri, StandardCharsets.UTF_8) +
                "&response_type=code&scope=User.Read Chat.Read offline_access&state=" + empId;

        return ResponseEntity.ok(authUrl);
    }

    // Microsoft redirects here after user approves
    @GetMapping("/microsoft/callback")
    public ResponseEntity<String> microsoftCallback(@RequestParam String code, @RequestParam String state) {
        integrationService.processMicrosoftCallback(state, code);
        return ResponseEntity.ok("<html><body><h3>Microsoft Teams Connected Successfully! You can close this tab.</h3></body></html>");
    }
}