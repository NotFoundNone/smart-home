package dev.project.second_receiver.repository;

import dev.project.second_receiver.entity.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceStateRepository extends JpaRepository<DeviceState, Long> {

    Optional<DeviceState> findByDeviceIdAndIsValidTrue(Long deviceId);
}
