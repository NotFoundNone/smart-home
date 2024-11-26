package dev.project.grpc.grpcservice.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "registry", schema = "dev")
public class Device {

    @Id
    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "device_location")
    private String deviceLocation;

    @Column(name = "device_manufacturer")
    private String deviceManufacturer;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_type")
    private Long deviceType;

    @Column(name = "is_valid")
    private Boolean isValid;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DeviceState> states;

    public Device() {}

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public List<DeviceState> getStates() {
        return states;
    }

    public void setStates(List<DeviceState> states) {
        this.states = states;
    }
}
