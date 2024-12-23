package dev.project.audit_log.repository;


import dev.project.audit_log.entity.DeviceEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<DeviceEvent, Long> {
}
