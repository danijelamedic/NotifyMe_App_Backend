package com.notifyme.isa.notifyme_backend.messaging;

import com.notifyme.isa.notifyme_backend.messaging.dto.UploadCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UploadEventConsumer {

    @RabbitListener(queues = "${notifyme.queue}")
    public void onUploadCreated(UploadCreatedEvent event) {
        System.out.println("[NotifyMe] Upload created: videoId=" + event.getVideoId()
                + ", author=" + event.getAuthorUsername()
                + ", createdAt=" + event.getCreatedAt());
    }
}
