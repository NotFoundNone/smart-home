package dev.project.second_receiver.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class StateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateService.class);

    private static final String QUEUE_NAME = "deviceControlQueue";
    private static final String EXCHANGE_NAME = "deviceEvents";
    private static final int MAX_READINGS = 5;
    private static final int AVERAGE_TEMPERATURE = 25;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Double> temperatureReadings = new ArrayList<>();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveDeviceStatus(String message) {
        try {
            LOGGER.info("Received message: {}", message);

            JsonNode jsonNode = objectMapper.readTree(message);

            if (!jsonNode.has("deviceId") || !jsonNode.has("value")) {
                LOGGER.error("Invalid message format: missing fields in {}", message);
            }

            Long deviceId = jsonNode.get("deviceId").asLong();
            double temperature = jsonNode.get("value").asDouble();

            addTemperatureReading(temperature);

            if (temperatureReadings.size() == MAX_READINGS) {
                checkAutomationConditions(deviceId);
            }
        } catch (Exception e) {
            LOGGER.error("Error processing message: {}", message, e);
        }
    }

    private void addTemperatureReading(double temperature) {

        temperatureReadings.add(temperature);

        if (temperatureReadings.size() > MAX_READINGS) {
            temperatureReadings.remove(0);
        }
    }

    private void checkAutomationConditions(Long deviceId) {
        double averageTemperature = temperatureReadings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        if (averageTemperature < AVERAGE_TEMPERATURE) {
            String message = String.format(Locale.US,"{\"action\": \"temperature\", \"averageTemperature\": %.2f}", averageTemperature);

            LOGGER.info("Отправка сообщения в очередь: {}", message);
            LOGGER.info("Warning! AverageTemperature: {}", averageTemperature);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "heater.room", message);
        }
    }
}
