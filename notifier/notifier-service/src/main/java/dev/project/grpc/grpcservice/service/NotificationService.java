package dev.project.grpc.grpcservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate template;

    public void sendNotification(String message) {
        template.convertAndSend("/topic/notifications", message);
    }

    public void sendTemperature(String temperature) {
        template.convertAndSend("/topic/temperature", "Current temperature: " + temperature);
    }
}
