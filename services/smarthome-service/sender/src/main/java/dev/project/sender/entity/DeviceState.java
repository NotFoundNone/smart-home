package dev.project.sender.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "state", schema = "dev")
public class DeviceState {

    @Id
    @Column(name = "device_state_id")
    private Long deviceStateId;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "state_type_id")
    private Long stateTypeId;

    @Column(name = "begit_dt")
    private LocalDateTime begitDt;

    @Column(name = "end_dt")
    private LocalDateTime endDt;

    @Column(name = "is_valid")
    private Boolean isValid;

    public DeviceState() {
    }

    public Long getDeviceStateId() {
        return deviceStateId;
    }

    public void setDeviceStateId(Long deviceStateId) {
        this.deviceStateId = deviceStateId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getStateTypeId() {
        return stateTypeId;
    }

    public void setStateTypeId(Long stateTypeId) {
        this.stateTypeId = stateTypeId;
    }

    public LocalDateTime getBegitDt() {
        return begitDt;
    }

    public void setBegitDt(LocalDateTime begitDt) {
        this.begitDt = begitDt;
    }

    public LocalDateTime getEndDt() {
        return endDt;
    }

    public void setEndDt(LocalDateTime endDt) {
        this.endDt = endDt;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
