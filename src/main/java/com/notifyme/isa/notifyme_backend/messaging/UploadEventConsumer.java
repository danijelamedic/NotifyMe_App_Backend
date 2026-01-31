package com.notifyme.isa.notifyme_backend.messaging;

import com.notifyme.isa.notifyme_backend.messaging.dto.UploadCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UploadEventConsumer {

    @RabbitListener(queues = "${notifyme.queue}",
            containerFactory = "rabbitListenerContainerFactory")
    public void onUploadCreated(UploadCreatedEvent event) {


//        if ("fail".equals(event.getAuthorUsername())) {
//            throw new RuntimeException("Simulated consumer failure");
//        }


        System.out.println("[NotifyMe] Upload created: videoId=" + event.getVideoId()
                + ", author=" + event.getAuthorUsername()
                + ", createdAt=" + event.getCreatedAt());

        System.out.println(" CONSUMED upload.created: videoId=" + event.getVideoId()
                + " sizeBytes=" + event.getSizeBytes()
                + " thumb=" + event.getThumbnailSizeBytes()
                + " createdAt=" + event.getCreatedAt());

    }
}
