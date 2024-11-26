package dev.project.second_receiver;

import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecondReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecondReceiverApplication.class, args);
	}

}
