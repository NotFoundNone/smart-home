package dev.project.sender.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfiguration {

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Bean
    public Queue myQueue() {
        return new Queue(queueName, false, false, false);
    }

    @Bean
    Exchange exchange() {
        return new TopicExchange(exchangeName, false, false);
    }

    @Bean
    Binding firstBinding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("device.events").noargs();
    }

    @Bean
    Binding secondBinding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("device.temperature").noargs();
    }
}
