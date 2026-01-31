package com.notifyme.isa.notifyme_backend.controller;

import com.notifyme.isa.notifyme_backend.messaging.UploadEventPublisher;
import com.notifyme.isa.notifyme_backend.messaging.UploadProtoEventPublisher;
import com.notifyme.isa.notifyme_backend.messaging.dto.UploadCreatedEvent;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class TestPublishController {

    private final UploadEventPublisher publisher;
    private final UploadProtoEventPublisher uploadProtoEventPublisher;

    public TestPublishController(UploadEventPublisher publisher,
                                 UploadProtoEventPublisher uploadProtoEventPublisher) {
        this.publisher = publisher;
        this.uploadProtoEventPublisher = uploadProtoEventPublisher;
    }

    // JSON
    @PostMapping("/publish-upload-created")
    public String publishUploadCreated(
            @RequestParam Long videoId,
            @RequestParam String authorUsername
    ) {
        UploadCreatedEvent event = new UploadCreatedEvent();
        event.setVideoId(videoId);
        event.setAuthorUsername(authorUsername);
        event.setCreatedAtFlexible(LocalDateTime.now());

        publisher.publish(event);
        return "Sent upload.created event (JSON)";
    }

    // Protobuf
    @PostMapping("/publish-upload-created-pb")
    public String publishPb() {
        notifyme.upload.proto.UploadCreatedEvent event =
                notifyme.upload.proto.UploadCreatedEvent.newBuilder()
                        .setVideoId(123)
                        .setTitle("PB test video")
                        .setSizeBytes(987654)
                        .setAuthorUsername("danijela")
                        .setCreatedAt(java.time.OffsetDateTime.now().toString())
                        .build();

        uploadProtoEventPublisher.publish(event);
        return "Published PB UploadCreatedEvent";
    }
}
