package dev.project.sender.service;

import dev.project.api.dto.DeviceDto;
import dev.project.api.exception.DeviceNotFoundException;
import dev.project.sender.configuration.SnowflakeGenerator;
import dev.project.sender.controller.SendController;
import dev.project.sender.entity.DeviceEvent;
import dev.project.sender.mapper.DeviceMapper;
import dev.project.sender.repository.DeviceRepository;
import dev.project.sender.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.project.sender.util.EventMapper.toEvent;

@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendController.class);
    static final String exchangeName = "deviceEvents";

    private final SnowflakeGenerator idGenerator;

    @Autowired
    public DeviceService(SnowflakeGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void addDevice(DeviceDto deviceDto) {
        Long id = idGenerator.nextId();
        Device newDevice = deviceMapper.toEntity(deviceDto);
        newDevice.setDeviceId(id);
        newDevice.setValid(true);
        deviceRepository.saveAndFlush(newDevice);
        LOGGER.info("Added new device with deviceId: {}", id);

        DeviceEvent event = toEvent("CREATE_DEVICE", newDevice);
        rabbitTemplate.convertAndSend(exchangeName, "device.events", event);
    }

    public DeviceDto getDevice(Long deviceId) throws DeviceNotFoundException {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
        return deviceMapper.toDto(device);
    }

    public void updateDevice(DeviceDto deviceDto) {
        Device updatedDevice = deviceMapper.toEntity(deviceDto);
        updatedDevice.setValid(true);
        deviceRepository.saveAndFlush(updatedDevice);
        LOGGER.warn("Device with deviceId = {} - updated!", updatedDevice.getDeviceId());

        DeviceEvent event = toEvent("UPDATE_DEVICE", updatedDevice);
        rabbitTemplate.convertAndSend(exchangeName, "device.events", event);
    }

    public void deleteDevice(Long deviceId) {
        Optional<Device> existingDevice = deviceRepository.findById(deviceId);

        if (existingDevice.isPresent())
        {
            Device device = existingDevice.get();
            device.setValid(false);
            deviceRepository.saveAndFlush(device);
            LOGGER.info("Device with deviceId = {} deleted.", deviceId);

            DeviceEvent event = toEvent("DELETE_DEVICE", device);
            rabbitTemplate.convertAndSend(exchangeName, "device.events", event);
        }
        else
        {
            LOGGER.info("Device with deviceId = {} does not exist.", deviceId);
        }
    }

    public List<DeviceDto> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream().map(deviceMapper::toDto).collect(Collectors.toList());
    }
}

