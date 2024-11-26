package dev.project.sender.repository;

import dev.project.sender.entity.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceStateRepository extends JpaRepository<DeviceState, Long> {

    Optional<DeviceState> findByDeviceIdAndIsValidTrue(Long deviceId);
}
