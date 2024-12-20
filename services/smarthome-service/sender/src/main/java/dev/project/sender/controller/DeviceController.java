package dev.project.sender.controller;

import dev.project.api.controller.DeviceApi;
import dev.project.api.dto.DeviceDto;
import dev.project.api.dto.DeviceResourceDtoRequest;
import dev.project.api.dto.GetAllDevicesResponse;
import dev.project.api.dto.RequestDto;
import dev.project.api.exception.DeviceNotFoundException;
import dev.project.sender.service.DeviceService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Log4j2
@RestController
public class DeviceController implements DeviceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    private final RabbitTemplate rabbitTemplate;
    private DeviceService deviceService;

    @Autowired
    public DeviceController(RabbitTemplate rabbitTemplate, DeviceService deviceService) {
        this.deviceService = deviceService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ResponseEntity<DeviceResourceDtoRequest> addDevice(DeviceDto device) {
        deviceService.addDevice(device);
        DeviceResourceDtoRequest resource = new DeviceResourceDtoRequest(device);

        resource.add(linkTo(methodOn(DeviceController.class).getDevice(device.getDeviceId())).withSelfRel());
        resource.add(linkTo(methodOn(DeviceController.class).getAllDevices()).withRel("devices"));

        resource.addAction("update", "PUT", linkTo(methodOn(DeviceController.class).updateDevice(device)).withRel("update"));
        resource.addAction("delete", "DELETE", linkTo(methodOn(DeviceController.class).deleteDevice(device.getDeviceId())).withRel("delete"));

        return ResponseEntity.ok().body(resource);
    }

    @Override
    public ResponseEntity<DeviceResourceDtoRequest> getDevice(@PathVariable Long deviceId) throws DeviceNotFoundException {
        DeviceDto device = deviceService.getDevice(deviceId);
        DeviceResourceDtoRequest resource = new DeviceResourceDtoRequest(device);

        resource.add(linkTo(methodOn(DeviceController.class).getDevice(device.getDeviceId())).withSelfRel());
        resource.add(linkTo(methodOn(DeviceController.class).getAllDevices()).withRel("devices"));

        resource.addAction("update", "PUT", linkTo(methodOn(DeviceController.class).updateDevice(device)).withRel("update"));
        resource.addAction("delete", "DELETE", linkTo(methodOn(DeviceController.class).deleteDevice(device.getDeviceId())).withRel("delete"));

        return ResponseEntity.ok().body(resource);
    }

    @Override
    public ResponseEntity<GetAllDevicesResponse> getAllDevices() {
        List<DeviceResourceDtoRequest> devices = deviceService.getAllDevices().stream()
                .map(device -> {
                    DeviceResourceDtoRequest resource = new DeviceResourceDtoRequest(device);

                    resource.add(linkTo(methodOn(DeviceController.class).getDevice(device.getDeviceId())).withSelfRel());
                    resource.add(linkTo(methodOn(DeviceController.class).getAllDevices()).withRel("devices"));

                    resource.addAction("update", "PUT", linkTo(methodOn(DeviceController.class).updateDevice(device)).withRel("update"));
                    resource.addAction("delete", "DELETE", linkTo(methodOn(DeviceController.class).deleteDevice(device.getDeviceId())).withRel("delete"));

                    return resource;
                })
                .collect(Collectors.toList());

        GetAllDevicesResponse devicesResponse = new GetAllDevicesResponse(devices);

        return ResponseEntity.ok().body(devicesResponse);
    }

    @Override
    public ResponseEntity<DeviceResourceDtoRequest> updateDevice(DeviceDto device) {
        deviceService.updateDevice(device);
        DeviceResourceDtoRequest resource = new DeviceResourceDtoRequest(device);

        resource.add(linkTo(methodOn(DeviceController.class).getDevice(device.getDeviceId())).withSelfRel());
        resource.add(linkTo(methodOn(DeviceController.class).getAllDevices()).withRel("devices"));

        resource.addAction("delete", "DELETE", linkTo(methodOn(DeviceController.class).deleteDevice(device.getDeviceId())).withRel("delete"));

        return ResponseEntity.ok().body(resource);
    }

    @Override
    public ResponseEntity<RequestDto> deleteDevice(@PathVariable Long deviceId) {
        deviceService.deleteDevice(deviceId);
        RequestDto request = new RequestDto();
        request.setDeviceId(deviceId);
        request.add(linkTo(methodOn(DeviceController.class).addDevice(new DeviceDto())).withRel("create"));
        request.add(linkTo(methodOn(DeviceController.class).getAllDevices()).withRel("devices"));
        return ResponseEntity.ok().body(request);
    }
}

