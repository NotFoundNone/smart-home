package dev.project.sender.controller;

import dev.project.api.controller.StateApi;
import dev.project.api.dto.DeviceDto;
import dev.project.sender.service.DeviceService;
import dev.project.sender.service.StateService;
import lombok.extern.log4j.Log4j2;
import dev.project.sender.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/device/state")
public class StateController implements StateApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateController.class);

    private final RabbitTemplate rabbitTemplate;
    private StateService stateService;
    private DeviceService deviceService;


    @Autowired
    public StateController(RabbitTemplate rabbitTemplate, StateService stateService, DeviceService deviceService) {
        this.stateService = stateService;
        this.rabbitTemplate = rabbitTemplate;
        this.deviceService = deviceService;
    }

    @Override
    public ResponseEntity<EntityModel<DeviceDto>> changeDeviceState(@RequestParam Long deviceId, @RequestParam Long stateTypeId) {
        stateService.changeDeviceState(deviceId, stateTypeId);
        DeviceDto device = deviceService.getDevice(deviceId);
        EntityModel<DeviceDto> resource = EntityModel.of(device);
        resource.add(linkTo(methodOn(StateController.class).toggleDevice(deviceId)).withRel("toggle"));
        resource.add(linkTo(methodOn(DeviceController.class).getDevice(deviceId)).withSelfRel());
        resource.add(linkTo(methodOn(DeviceController.class).getAllDevices()).withRel("devices"));
        return ResponseEntity.ok().body(resource);
    }

    @Override
    public ResponseEntity<EntityModel<DeviceDto>> toggleDevice(@RequestParam Long deviceId) {
        DeviceDto device = deviceService.getDevice(deviceId);
        EntityModel<DeviceDto> resource = EntityModel.of(device);
        resource.add(linkTo(methodOn(DeviceController.class).getDevice(deviceId)).withSelfRel());
        resource.add(linkTo(methodOn(DeviceController.class).getAllDevices()).withRel("devices"));
        return ResponseEntity.ok().body(resource);
    }

    @Override
    public ResponseEntity<EntityModel<DeviceDto>> sendDeviceTemperature(@RequestParam Long deviceId, @RequestParam double temperature) {
        stateService.sendTemperatureUpdate(deviceId, temperature);
        DeviceDto device = deviceService.getDevice(deviceId);
        EntityModel<DeviceDto> resource = EntityModel.of(device);
        resource.add(linkTo(methodOn(DeviceController.class).getDevice(deviceId)).withSelfRel());
        resource.add(linkTo(methodOn(DeviceController.class).getAllDevices()).withRel("devices"));
        return ResponseEntity.ok().body(resource);
    }
}
