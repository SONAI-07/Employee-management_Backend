package com.example.employeger.service;

import com.example.employeger.model.Emp_model;
import com.example.employeger.repository.Comp_Repo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class IntegrationService {

    private final Comp_Repo empRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${github.client-id}")
    private String githubClientId;
    @Value("${github.client-secret}")
    private String githubClientSecret;

    @Value("${microsoft.client-id}")
    private String msClientId;
    @Value("${microsoft.client-secret}")
    private String msClientSecret;
    @Value("${microsoft.redirect-uri}")
    private String msRedirectUri;

    public IntegrationService(Comp_Repo empRepo) {
        this.empRepo = empRepo;
    }

    public void processGitHubCallback(String empId, String code) {
        // 1. Exchange temporary code for Access Token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", githubClientId);
        body.add("client_secret", githubClientSecret);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://github.com/login/oauth/access_token", request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String accessToken = (String) response.getBody().get("access_token");
            String username = fetchGitHubUsername(accessToken);

            // 2. Save to Database
            Emp_model emp = empRepo.findById(empId).orElseThrow(() -> new RuntimeException("Employee not found"));
            emp.setGithub_username(username);
            emp.setGithub_access_token(accessToken);
            emp.setGithub_connected(true);
            empRepo.save(emp);
        } else {
            throw new RuntimeException("Failed to get GitHub access token");
        }
    }

    private String fetchGitHubUsername(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Accept", "application/vnd.github.v3+json");

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("login");
        }
        return "unknown";
    }

    public void processMicrosoftCallback(String empId, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", msClientId);
        body.add("client_secret", msClientSecret);
        body.add("code", code);
        body.add("redirect_uri", msRedirectUri);
        body.add("grant_type", "authorization_code");
        // Scopes required to read Teams messages and user profile
        body.add("scope", "User.Read Chat.Read offline_access");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://login.microsoftonline.com/common/oauth2/v2.0/token", request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String accessToken = (String) response.getBody().get("access_token");

            Emp_model emp = empRepo.findById(empId).orElseThrow(() -> new RuntimeException("Employee not found"));
            emp.setMs_teams_access_token(accessToken);
            emp.setTeams_connected(true);
            empRepo.save(emp);
        } else {
            throw new RuntimeException("Failed to get Microsoft access token");
        }
    }
}