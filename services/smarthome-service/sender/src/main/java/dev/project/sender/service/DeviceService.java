package dev.project.sender.service;

import dev.project.api.dto.DeviceDto;
import dev.project.api.exception.DeviceNotFoundException;
import dev.project.sender.configuration.SnowflakeGenerator;
import dev.project.sender.controller.SendController;
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
    private RabbitTemplate rabbitTemplate;

    public void addDevice(DeviceDto device) {
        Long id = idGenerator.nextId();
        Device newDevice = new Device();
        newDevice.setDeviceId(id);
        newDevice.setDeviceName(device.getDeviceName());
        newDevice.setDeviceType(device.getDeviceType());
        newDevice.setDeviceManufacturer(device.getDeviceManufacturer());
        newDevice.setDeviceLocation(device.getDeviceLocation());
        newDevice.setValid(true);

        deviceRepository.saveAndFlush(newDevice);
        LOGGER.info("Added new device with deviceId: {}, deviceName: {}, deviceManufacturer: {}, deviceType: {}, deviceLocation: {}.", device.getDeviceId(), device.getDeviceName(), device.getDeviceManufacturer(), device.getDeviceType(), device.getDeviceLocation());
    }

    public DeviceDto getDevice(Long deviceId) throws DeviceNotFoundException{
        Optional<Device> existingDevice = deviceRepository.findById(deviceId);
        if(existingDevice.isPresent())
        {
            Device device = existingDevice.get();
            DeviceDto deviceDto = new DeviceDto();
            deviceDto.setDeviceId(device.getDeviceId());
            deviceDto.setDeviceName(device.getDeviceName());
            deviceDto.setDeviceType(device.getDeviceType());
            deviceDto.setDeviceManufacturer(device.getDeviceManufacturer());
            deviceDto.setDeviceLocation(device.getDeviceLocation());
            return deviceDto;
        }
        else {
            throw new DeviceNotFoundException(deviceId);
        }
    }

    public void updateDevice(DeviceDto device) {
        if (device.getDeviceId() != null)
        {
            Optional<Device> existingDevice = deviceRepository.findById(device.getDeviceId());
            if(existingDevice.isPresent())
            {
                Device updatedDevice = new Device();
                updatedDevice.setDeviceId(device.getDeviceId());
                updatedDevice.setDeviceName(device.getDeviceName());
                updatedDevice.setDeviceType(device.getDeviceType());
                updatedDevice.setDeviceManufacturer(device.getDeviceManufacturer());
                updatedDevice.setDeviceLocation(device.getDeviceLocation());
                updatedDevice.setValid(true);
                deviceRepository.saveAndFlush(updatedDevice);
                LOGGER.warn("Device with deviceId = {} - updated!", updatedDevice.getDeviceId());
            }
            else {
                LOGGER.warn("Device with deviceId = {} - not found!", device.getDeviceId());
            }
        }
    }

    public void deleteDevice(Long deviceId) {
        Optional<Device> existingDevice = deviceRepository.findById(deviceId);

        if (existingDevice.isPresent())
        {
            Device device = existingDevice.get();
            device.setValid(false);
            deviceRepository.saveAndFlush(device);
            LOGGER.info("Device with deviceId = {} deleted.", deviceId);
        }
        else
        {
            LOGGER.info("Device with deviceId = {} does not exist.", deviceId);
        }
    }

    public List<DeviceDto> getAllDevices() {
        LOGGER.info("Getting all devices.");
        List<Device> devices = deviceRepository.findAll();

        // Преобразуем каждое устройство в DeviceDto
        return devices.stream()
                .map(device -> {
                    DeviceDto dto = new DeviceDto();
                    dto.setDeviceId(device.getDeviceId());
                    dto.setDeviceName(device.getDeviceName());
                    dto.setDeviceManufacturer(device.getDeviceManufacturer());
                    dto.setDeviceType(device.getDeviceType());
                    dto.setDeviceLocation(device.getDeviceLocation());
                    dto.setValid(device.getValid());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void toggleDevice(Long deviceId) {
        LOGGER.info("Toggle device with id = {}.", deviceId);

        rabbitTemplate.convertAndSend(exchangeName, "device.events", "Device with deviceId: " + deviceId + " - toggled.");
    }

}

