package dev.project.grpc.grpcservice.repository;

import dev.project.grpc.grpcservice.entity.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceStateRepository extends JpaRepository<DeviceState, Long> {

    // Найти состояния по типу устройства и типу состояния
    @Query("""
        SELECT s FROM DeviceState s
        WHERE s.stateTypeId = :stateTypeId
          AND s.device.deviceType IN (:deviceTypes)
          AND s.isValid = true
    """)
    List<DeviceState> findStatesByTypeAndDeviceTypes(
            @Param("stateTypeId") Long stateTypeId,
            @Param("deviceTypes") List<Long> deviceTypes
    );

    // Найти активные устройства (stateTypeId = 1)
    @Query("""
        SELECT s FROM DeviceState s
        WHERE s.stateTypeId = 1
          AND s.device.deviceType = :deviceType
          AND s.isValid = true
    """)
    List<DeviceState> findActiveStatesByDeviceType(@Param("deviceType") Long deviceType);

    // Найти первое валидное неактивное устройство для активации
    @Query("""
        SELECT s FROM DeviceState s
        WHERE s.stateTypeId != 1
          AND s.device.deviceType = :deviceType
          AND s.isValid = true
        ORDER BY s.deviceStateId ASC
    """)
    List<DeviceState> findInactiveStatesForActivation(@Param("deviceType") Long deviceType);
}
