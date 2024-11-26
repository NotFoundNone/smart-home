package dev.project.grpc.grpcservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "state", schema = "dev")
public class DeviceState {

    @Id
    @Column(name = "device_state_id")
    private Long deviceStateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", referencedColumnName = "device_id")
    private Device device;

    @Column(name = "state_type_id")
    private Long stateTypeId;

    @Column(name = "state_type_name")
    private String stateTypeName;

    @Column(name = "begit_dt")
    private LocalDateTime begitDt;

    @Column(name = "end_dt")
    private LocalDateTime endDt;

    @Column(name = "is_valid")
    private Boolean isValid;

    public DeviceState() {}

    public Long getDeviceStateId() {
        return deviceStateId;
    }

    public void setDeviceStateId(Long deviceStateId) {
        this.deviceStateId = deviceStateId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Long getStateTypeId() {
        return stateTypeId;
    }

    public void setStateTypeId(Long stateTypeId) {
        this.stateTypeId = stateTypeId;
    }

    public String getStateTypeName() {
        return stateTypeName;
    }

    public void setStateTypeName(String stateTypeName) {
        this.stateTypeName = stateTypeName;
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
