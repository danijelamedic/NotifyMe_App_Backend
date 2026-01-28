package com.notifyme.isa.notifyme_backend.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UploadEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${notifyme.exchange}")
    private String exchangeName;

    @Value("${notifyme.routing-key}")
    private String routingKey;

    public UploadEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
