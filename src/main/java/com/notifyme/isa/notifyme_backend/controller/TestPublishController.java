package com.notifyme.isa.notifyme_backend.controller;

import com.notifyme.isa.notifyme_backend.messaging.UploadEventPublisher;
import com.notifyme.isa.notifyme_backend.messaging.dto.UploadCreatedEvent;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/test")
public class TestPublishController {

    private final UploadEventPublisher publisher;

    public TestPublishController(UploadEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/publish-upload-created")
    public String publishUploadCreated(
            @RequestParam Long videoId,
            @RequestParam String authorUsername
    ) {
        UploadCreatedEvent event = new UploadCreatedEvent();
        event.setVideoId(videoId);
        event.setAuthorUsername(authorUsername);
        event.setCreatedAt(Instant.now());

        publisher.publish(event);
        return "Sent upload.created event";
    }
}
