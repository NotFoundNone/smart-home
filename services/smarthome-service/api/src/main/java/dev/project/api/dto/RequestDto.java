package dev.project.api.dto;

import org.springframework.hateoas.RepresentationModel;

public class RequestDto extends RepresentationModel<RequestDto> {

    private Long deviceId;
    private String message;

    public RequestDto() {
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}