package dev.project.grpc.grpcservice.repository;

import dev.project.grpc.grpcservice.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    // Найти все валидные устройства определённого типа
    @Query("""
        SELECT d FROM Device d
        WHERE d.deviceType = :deviceType
          AND d.isValid = true
    """)
    List<Device> findValidDevicesByType(@Param("deviceType") Long deviceType);
}
