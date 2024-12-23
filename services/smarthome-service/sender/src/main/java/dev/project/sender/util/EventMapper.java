package dev.project.sender.util;

import dev.project.sender.entity.Device;
import dev.project.sender.entity.DeviceEvent;

import java.time.LocalDateTime;

public class EventMapper {

    public static DeviceEvent toEvent(String eventType, Device device) {
        if (device == null) {
            throw new IllegalArgumentException("DeviceDto cannot be null");
        }

        DeviceEvent deviceEvent = new DeviceEvent();
        deviceEvent.setEventType(eventType);
        deviceEvent.setDeviceId(device.getDeviceId());
        deviceEvent.setDeviceName(device.getDeviceName());
        deviceEvent.setDeviceManufacturer(device.getDeviceManufacturer());
        deviceEvent.setDeviceType(device.getDeviceType());
        deviceEvent.setDeviceLocation(device.getDeviceLocation());
        deviceEvent.setIsValid(device.getValid());
        deviceEvent.setEventTime(LocalDateTime.now());

        return deviceEvent;
    }
}
