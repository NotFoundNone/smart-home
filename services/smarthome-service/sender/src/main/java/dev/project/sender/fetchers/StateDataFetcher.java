package dev.project.sender.fetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import dev.project.api.dto.DeviceDto;
import dev.project.sender.service.DeviceService;
import dev.project.sender.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;

@DgsComponent
public class StateDataFetcher {

    private final StateService stateService;
    private final DeviceService deviceService;

    @Autowired
    public StateDataFetcher(StateService stateService, DeviceService deviceService) {
        this.stateService = stateService;
        this.deviceService = deviceService;
    }

    @DgsMutation
    public DeviceDto changeDeviceState(@InputArgument Long deviceId, @InputArgument Long stateTypeId) {
        stateService.changeDeviceState(deviceId, stateTypeId);
        return deviceService.getDevice(deviceId);
    }

    @DgsMutation
    public DeviceDto toggleDevice(@InputArgument Long deviceId) {
        deviceService.toggleDevice(deviceId);
        return deviceService.getDevice(deviceId);
    }
}
