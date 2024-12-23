package dev.project.audit_log.service;

import dev.project.audit_log.entity.DeviceEvent;
import dev.project.audit_log.repository.LogEntryRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final LogEntryRepository logEntryRepository;

    public AuditLogService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    public DeviceEvent saveLog(DeviceEvent deviceEvent) {
        return logEntryRepository.save(deviceEvent);
    }
}
