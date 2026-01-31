package com.notifyme.isa.notifyme_backend.messaging;

import notifyme.upload.proto.UploadCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.notifyme.isa.notifyme_backend.config.RabbitMQConfig.UPLOAD_CREATED_PB_ROUTING_KEY;

@Service
public class UploadProtoEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;

    public UploadProtoEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${notifyme.exchange}") String exchangeName
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
    }

    public void publish(UploadCreatedEvent event) {
        byte[] payload = event.toByteArray();
        rabbitTemplate.convertAndSend(exchangeName, UPLOAD_CREATED_PB_ROUTING_KEY, payload);
    }
}
