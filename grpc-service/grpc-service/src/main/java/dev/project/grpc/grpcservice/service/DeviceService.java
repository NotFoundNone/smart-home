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

    private static final Long OPEN_STATE_TYPE = 7L;
    private static final Long DOOR_SENSOR = 21L;
    private static final Long WINDOW_SENSOR = 22L;
    private static final Long HEATER = 2L;


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

    public void processTemperatureAction(Double averageTemperature) {

        if (hasOpenWindowsOrDoors()) {
            LOGGER.info("Open windows or doors detected!");
            sendNotification("Open windows or doors detected!");
        }
        else {
            if (averageTemperature < MIN_TEMPERATURE) {
                if (!hasActiveHeatingDevices()) {
                    LOGGER.info("Activating heating system!");
                    activateAnyHeatingDevice();
                }
            }
        }
    }

    private boolean hasOpenWindowsOrDoors() {
        List<DeviceState> openStates = deviceStateRepository.findStatesByTypeAndDeviceTypes(OPEN_STATE_TYPE, List.of(DOOR_SENSOR, WINDOW_SENSOR));
        return !openStates.isEmpty();
    }

    private boolean hasActiveHeatingDevices() {
        LOGGER.info("Heating on = {}" , !deviceStateRepository.findActiveStatesByDeviceType(HEATER).isEmpty());
        return !deviceStateRepository.findActiveStatesByDeviceType(HEATER).isEmpty();
    }

    private void activateAnyHeatingDevice() {
        List<DeviceState> inactiveStates = deviceStateRepository.findInactiveStatesForActivation(HEATER);
        if (!inactiveStates.isEmpty()) {
            DeviceState oldState = inactiveStates.get(0);

            oldState.setValid(false);
            oldState.setEndDt(LocalDateTime.now());
            deviceStateRepository.save(oldState);

            DeviceState newState = new DeviceState();
            newState.setDeviceStateId(idGenerator.nextId());
            newState.setStateTypeId(1L);
            newState.setDevice(oldState.getDevice());
            newState.setBegitDt(LocalDateTime.now());
            newState.setValid(true);
            deviceStateRepository.save(newState);

            sendNotification("Heating system activated!");
        }
    }

    private void sendNotification(String message) {
        LOGGER.info("Send message = {}", message);
        rabbitTemplate.convertAndSend(exchangeName,"notifications.queue", message);
    }
}
