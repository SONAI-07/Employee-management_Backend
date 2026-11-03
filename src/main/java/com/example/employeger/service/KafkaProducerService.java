package com.example.employeger.service;

import com.example.employeger.model.ActivityEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "employee-activity-stream";
    private final KafkaTemplate<String, ActivityEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, ActivityEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(ActivityEvent event) {
        kafkaTemplate.send(TOPIC, event.getEmployeeId(), event);
        System.out.println("Event pushed to Kafka: " + event.getEventType() + " for employee " + event.getEmployeeId());
    }
}