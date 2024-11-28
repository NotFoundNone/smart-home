package dev.project.grpc.grpcservice;

import dev.project.grpc.grpcservice.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class NotifierServiceApplication {

	static final String queueName = "notificationQueue";

	@Autowired
	private NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(NotifierServiceApplication.class, args);
	}

	@Bean
	public Queue myQueue() {
		return new Queue(queueName, false);
	}

	@RabbitListener(queues = queueName)
	public void listen(String message) {
		System.out.println("Message read from firstQueue : " + message);
		notificationService.sendNotification(message);
	}

}