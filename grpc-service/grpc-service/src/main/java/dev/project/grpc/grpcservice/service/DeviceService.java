package dev.project.grpc.grpcservice.service;

import dev.project.grpc.grpcservice.repository.DeviceRepository;
import dev.project.grpc.grpcservice.repository.DeviceStateRepository;
import dev.project.grpc.grpcservice.entity.DeviceState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    private static final Double MIN_TEMPERATURE = 20.0;

    static final String exchangeName = "deviceEvents";

    private final DeviceRepository deviceRepository;
    private final DeviceStateRepository deviceStateRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository,
                         DeviceStateRepository deviceStateRepository,
                         RabbitTemplate rabbitTemplate) {
        this.deviceRepository = deviceRepository;
        this.deviceStateRepository = deviceStateRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // Основной метод обработки действий
    public void processTemperatureAction(Double averageTemperature) {

        // Проверяем открытые окна или двери
        if (hasOpenWindowsOrDoors()) {
            LOGGER.info("Open windows or doors detected!");
            sendNotification("Open windows or doors detected!");
            try {
                LOGGER.info("Waiting 5 seconds before activating heating...");
                Thread.sleep(5000); // Задержка в 5 секунд
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Heating activation delay interrupted", e);
                return;
            }
        }

        // Если температура ниже 25, проверяем систему отопления
        if (averageTemperature < MIN_TEMPERATURE) {
            if (!hasActiveHeatingDevices()) {
                LOGGER.info("Activated heating system!");
                activateAnyHeatingDevice();
            }
        }
    }

    // Проверка открытых окон или дверей
    private boolean hasOpenWindowsOrDoors() {
        List<DeviceState> openStates = deviceStateRepository.findStatesByTypeAndDeviceTypes(7L, List.of(21L, 22L));
        return !openStates.isEmpty();
    }

    // Проверка наличия активных отопительных устройств
    private boolean hasActiveHeatingDevices() {
        LOGGER.info("Heating on = {}" , !deviceStateRepository.findActiveStatesByDeviceType(2L).isEmpty());
        return !deviceStateRepository.findActiveStatesByDeviceType(2L).isEmpty();
    }

    // Активация любого отопительного устройства
    private void activateAnyHeatingDevice() {
        List<DeviceState> inactiveStates = deviceStateRepository.findInactiveStatesForActivation(2L);
        if (!inactiveStates.isEmpty()) {
            DeviceState state = inactiveStates.get(0);
            state.setStateTypeId(1L); // Установить состояние "активное"
            deviceStateRepository.save(state);
            sendNotification("Heating system activated!");
        }
    }

    // Отправка уведомлений в RabbitMQ
    private void sendNotification(String message) {
        LOGGER.info("Send message = {}", message);
        rabbitTemplate.convertAndSend(exchangeName,"notifications.queue", message);
    }
}
