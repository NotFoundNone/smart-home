package dev.project.sender.mapper;

import dev.project.api.dto.DeviceDto;
import dev.project.sender.entity.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    // Преобразование из Device в DeviceDto
    public DeviceDto toDto(Device device) {
        if (device == null) {
            return null;
        }

        DeviceDto dto = new DeviceDto();
        dto.setDeviceId(device.getDeviceId());
        dto.setDeviceName(device.getDeviceName());
        dto.setDeviceType(device.getDeviceType());
        dto.setDeviceManufacturer(device.getDeviceManufacturer());
        dto.setDeviceLocation(device.getDeviceLocation());
        dto.setValid(device.getValid());
        return dto;
    }

    // Преобразование из DeviceDto в Device
    public Device toEntity(DeviceDto dto) {
        if (dto == null) {
            return null;
        }

        Device device = new Device();
        device.setDeviceId(dto.getDeviceId());
        device.setDeviceName(dto.getDeviceName());
        device.setDeviceType(dto.getDeviceType());
        device.setDeviceManufacturer(dto.getDeviceManufacturer());
        device.setDeviceLocation(dto.getDeviceLocation());
        device.setValid(dto.getValid());
        return device;
    }
}
