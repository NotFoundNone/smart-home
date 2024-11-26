package dev.project.api.dto;

import java.util.List;

public class GetAllDevicesResponse {

    List<DeviceResourceDtoRequest> devices;

    public GetAllDevicesResponse(List<DeviceResourceDtoRequest> devices) {
        this.devices = devices;
    }

    public List<DeviceResourceDtoRequest> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceResourceDtoRequest> devices) {
        this.devices = devices;
    }
}
