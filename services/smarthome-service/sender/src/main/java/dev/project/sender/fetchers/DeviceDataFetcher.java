package dev.project.sender.fetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import dev.project.api.dto.DeviceDto;
import dev.project.api.dto.DeviceInputDto;
import dev.project.sender.entity.Device;
import dev.project.sender.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DgsComponent
public class DeviceDataFetcher {

    @Autowired
    private DeviceService deviceService;

    @DgsQuery
    public DeviceDto getDevice(@InputArgument Long deviceId) {
        return deviceService.getDevice(deviceId);
    }

    @DgsQuery
    public List<DeviceDto> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @DgsMutation
    public DeviceDto addDevice(@InputArgument DeviceInputDto deviceInput) {
        DeviceDto newDevice = new DeviceDto();

        newDevice.setDeviceName(deviceInput.getDeviceName());
        newDevice.setDeviceManufacturer(deviceInput.getDeviceManufacturer());
        newDevice.setDeviceType(Long.valueOf(deviceInput.getDeviceType()));
        newDevice.setDeviceLocation(deviceInput.getDeviceLocation());

        deviceService.addDevice(newDevice);
        return newDevice;
    }

    @DgsMutation
    public DeviceDto updateDevice(@InputArgument("deviceId") Long deviceId, @InputArgument("device") DeviceInputDto deviceInput) {
        DeviceDto updatedDevice = new DeviceDto();
        updatedDevice.setDeviceId(deviceId);

        updatedDevice.setDeviceName(deviceInput.getDeviceName() != null ? deviceInput.getDeviceName() : null);
        updatedDevice.setDeviceManufacturer(deviceInput.getDeviceManufacturer() != null ? deviceInput.getDeviceManufacturer() : null);
        updatedDevice.setDeviceType(deviceInput.getDeviceType() != null ? Long.valueOf(deviceInput.getDeviceType()) : null);
        updatedDevice.setDeviceLocation(deviceInput.getDeviceLocation() != null ? deviceInput.getDeviceLocation() : null);

        deviceService.updateDevice(updatedDevice);
        return updatedDevice;
    }

    @DgsMutation
    public String deleteDevice(Long deviceId) {
        deviceService.deleteDevice(deviceId);
        return "Device deleted successfully";
    }
}