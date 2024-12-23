package dev.project.sender.service;

import dev.project.sender.configuration.SnowflakeGenerator;
import dev.project.sender.controller.SendController;
import dev.project.sender.repository.DeviceStateRepository;
import dev.project.sender.repository.StateTypeRepository;
import dev.project.sender.entity.DeviceState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class StateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendController.class);
    static final String exchangeName = "deviceEvents";

    private final SnowflakeGenerator idGenerator;

    @Autowired
    private DeviceStateRepository deviceStateRepository;

    @Autowired
    private StateTypeRepository stateTypeRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public StateService(SnowflakeGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void changeDeviceState(Long deviceId, Long stateTypeId)
    {
        LOGGER.info("Start changing state for device with deviceId = {}", deviceId);

        LocalDateTime tempDate = LocalDateTime.now();

        Optional<DeviceState> existingDeviceState = deviceStateRepository.findByDeviceIdAndIsValidTrue(deviceId);

        if (stateTypeId == 5)
        {
            rabbitTemplate.convertAndSend(exchangeName, "overheat.key", "Device with deviceId: " + deviceId + " - overheated!");
        }

        if (existingDeviceState.isPresent())
        {
            DeviceState deviceState = existingDeviceState.get();
            deviceState.setEndDt(tempDate);
            deviceState.setValid(false);

            createNewDeviceState(deviceId, stateTypeId);

            //TODO: Обработать NPE
            String oldStateName = stateTypeRepository.findById(deviceState.getStateTypeId()).get().getStateName();
            String newStateName = stateTypeRepository.findById(stateTypeId).get().getStateName();

            LOGGER.info("Change state for device with deviceId = {} from type: {} to type: {}", deviceId, oldStateName, newStateName);
        }
        else
        {
            LOGGER.info("State for device with deviceId = {} does not exist. Create a new state with type = {}", deviceId, stateTypeId);
            createNewDeviceState(deviceId, stateTypeId);
        }
    }

    private void createNewDeviceState(Long deviceId, Long stateTypeId)
    {
        DeviceState newDeviceState = new DeviceState();
        Long id = idGenerator.nextId();
        newDeviceState.setDeviceStateId(id);
        newDeviceState.setDeviceId(deviceId);
        newDeviceState.setStateTypeId(stateTypeId);
        newDeviceState.setBegitDt(LocalDateTime.now());
        newDeviceState.setValid(true);

        deviceStateRepository.saveAndFlush(newDeviceState);
    }

    public void sendTemperatureUpdate(Long deviceId, double temperature) {
        String message = String.format(Locale.US, "{\"deviceId\": %d, \"value\": %.2f}", deviceId, temperature);
        LOGGER.info("Sending message: {}", message);
        rabbitTemplate.convertAndSend(exchangeName, "device.temperature", message);
    }
}
