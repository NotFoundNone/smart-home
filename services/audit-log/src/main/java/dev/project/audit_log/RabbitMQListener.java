package dev.project.audit_log;

import dev.project.audit_log.entity.DeviceEvent;
import dev.project.audit_log.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQListener.class);

    private final AuditLogService auditLogService;

    public RabbitMQListener(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @RabbitListener(queues = "eventsQueue")
    public void receiveMessage(DeviceEvent logEntry, @Header("amqp_receivedRoutingKey") String routingKey) {
        if ("device.events".equals(routingKey)) {
            auditLogService.saveLog(logEntry);
            LOGGER.info("Log saved: {}", logEntry);
        }
    }
}