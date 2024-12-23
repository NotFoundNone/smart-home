package dev.project.grpc.grpcclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.project.grpc.grpcservice.DeviceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqListener.class);

    private final GrpcClient grpcClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public RabbitMqListener(GrpcClient grpcClient) {
        this.grpcClient = grpcClient;
    }

    @RabbitListener(queues = "heaterQueue")
    public void receiveMessage(String message) {
        try {

            LOGGER.info("Получено сообщение из очереди: {}", message);
            JsonNode jsonNode = objectMapper.readTree(message);
            String action = jsonNode.get("action").asText();
            double averageTemperature = jsonNode.get("averageTemperature").asDouble();

            if ("temperature".equals(action)) {
                DeviceResponse response = grpcClient.turnOnDevice(averageTemperature);
                LOGGER.info("Ответ от gRPC: {}", response.getMessage());
            } else {
                LOGGER.info("Неизвестное действие: {}", action);
            }
        } catch (Exception e) {
            LOGGER.info("Ошибка при обработке сообщения: {}", e.getMessage(), e);
        }
    }
}