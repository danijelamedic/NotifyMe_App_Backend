package com.notifyme.isa.notifyme_backend.messaging;

import notifyme.upload.proto.UploadCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.notifyme.isa.notifyme_backend.config.RabbitMQConfig.UPLOAD_EVENTS_PB_QUEUE;

@Component
public class UploadProtoEventConsumer {

    @RabbitListener(queues = UPLOAD_EVENTS_PB_QUEUE)
    public void onMessage(byte[] payload) {
        try {
            UploadCreatedEvent event = UploadCreatedEvent.parseFrom(payload);

            System.out.println("[PB] Received UploadCreatedEvent: " +
                    "videoId=" + event.getVideoId() +
                    ", title=" + event.getTitle() +
                    ", sizeBytes=" + event.getSizeBytes() +
                    ", author=" + event.getAuthorUsername() +
                    ", createdAt=" + event.getCreatedAt());

        } catch (Exception ex) {
            System.out.println("[PB] Failed to parse protobuf message: " + ex.getMessage());
            // kasnije: throw da ode u DLQ, ili custom error handling
        }
    }
}
