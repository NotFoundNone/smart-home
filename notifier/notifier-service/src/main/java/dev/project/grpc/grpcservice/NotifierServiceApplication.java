package dev.project.grpc.grpcservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.project.grpc.grpcservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class NotifierServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifierServiceApplication.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    static final String queueName = "notificationQueue";
    static final String temperatureQueueName = "averageTemperature";

    @Autowired
    private NotificationService notificationService;

    public static void main(String[] args) {
        SpringApplication.run(NotifierServiceApplication.class, args);
    }

    @Bean
    public Queue myQueue() {
        return new Queue(queueName, false);
    }

    @RabbitListener(queues = queueName)
    public void listen(String message) {
        System.out.println("Message read from firstQueue : " + message);
        notificationService.sendNotification(message);
    }

    @RabbitListener(queues = temperatureQueueName)
    public void listenTemperature(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String averageTemperature = jsonNode.get("averageTemperature").asText();
            LOGGER.info("Temperature read from temperatureQueue : " + averageTemperature);
            notificationService.sendTemperature(averageTemperature);
        } catch (Exception e) {
            LOGGER.error("Ошибка при обработке сообщения: {}", message, e);
        }
    }
}