package com.notifyme.isa.notifyme_backend.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UploadEventConsumer {

    @RabbitListener(queues = "${notifyme.queue}")
    public void onMessage(String message) {
        System.out.println("[NotifyMe] Received: " + message);
    }
}
