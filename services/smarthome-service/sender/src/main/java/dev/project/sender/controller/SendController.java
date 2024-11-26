package dev.project.sender.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/send")
public class SendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendController.class);

    private final RabbitTemplate rabbitTemplate;
    private static Long counter = 0L;
    static final String exchangeName = "testExchange";

    @Autowired
    public SendController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/q2")
    public ResponseEntity<String> sendMessageToQ2(@RequestParam String message) {
        rabbitTemplate.convertAndSend(exchangeName, "second.key", message);
        LOGGER.info(message + " is sent! Count send object = " + ++counter);
        return ResponseEntity.ok().body("Send to Q2!");
    }
}
