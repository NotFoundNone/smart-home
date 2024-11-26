package dev.project.api.dto;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashMap;
import java.util.Map;

public class DeviceResourceDtoRequest extends RepresentationModel<DeviceResourceDtoRequest> {
    private final DeviceDto device;
    private final Map<String, Map<String, String>> _actions = new HashMap<>();

    public DeviceResourceDtoRequest(DeviceDto device) {
        this.device = device;
    }

    public DeviceDto getDevice() {
        return device;
    }

    public Map<String, Map<String, String>> get_actions() {
        return _actions;
    }

    public void addAction(String actionName, String method, Link link) {
        Map<String, String> actionDetails = new HashMap<>();
        actionDetails.put("href", link.getHref());
        actionDetails.put("method", method);
        _actions.put(actionName, actionDetails);
    }
}