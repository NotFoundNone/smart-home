package dev.project.grpc.grpcservice.service;

import dev.project.grpc.grpcservice.config.SnowflakeGenerator;
import dev.project.grpc.grpcservice.repository.DeviceRepository;
import dev.project.grpc.grpcservice.repository.DeviceStateRepository;
import dev.project.grpc.grpcservice.entity.DeviceState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    private static final Double MIN_TEMPERATURE = 20.0;

    static final String exchangeName = "deviceEvents";

    private final DeviceRepository deviceRepository;
    private final DeviceStateRepository deviceStateRepository;
    private final RabbitTemplate rabbitTemplate;
    private final SnowflakeGenerator idGenerator;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository,
                         DeviceStateRepository deviceStateRepository,
                         RabbitTemplate rabbitTemplate,
                         SnowflakeGenerator idGenerator) {
        this.deviceRepository = deviceRepository;
        this.deviceStateRepository = deviceStateRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.idGenerator = idGenerator;
    }

    // Основной метод обработки действий
    public void processTemperatureAction(Double averageTemperature) {

        // Проверяем открытые окна или двери
        if (hasOpenWindowsOrDoors()) {
            LOGGER.info("Open windows or doors detected!");
            sendNotification("Open windows or doors detected!");
        }
        else {
            // Если температура ниже 20, проверяем систему отопления
            if (averageTemperature < MIN_TEMPERATURE) {
                if (!hasActiveHeatingDevices()) {
                    LOGGER.info("Activating heating system!");
                    activateAnyHeatingDevice();
                }
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
            DeviceState oldState = inactiveStates.get(0);

            // Инвалидация старого состояния
            oldState.setValid(false);
            oldState.setEndDt(LocalDateTime.now());
            deviceStateRepository.save(oldState);

            // Создание нового состояния
            DeviceState newState = new DeviceState();
            newState.setDeviceStateId(idGenerator.nextId());
            newState.setStateTypeId(1L); // Установить состояние "активное"
            newState.setDevice(oldState.getDevice());
            newState.setBegitDt(LocalDateTime.now());
            newState.setValid(true);
            deviceStateRepository.save(newState);

            // Отправка уведомления
            sendNotification("Heating system activated!");
        }
    }

    // Отправка уведомлений в RabbitMQ
    private void sendNotification(String message) {
        LOGGER.info("Send message = {}", message);
        rabbitTemplate.convertAndSend(exchangeName,"notifications.queue", message);
    }
}
