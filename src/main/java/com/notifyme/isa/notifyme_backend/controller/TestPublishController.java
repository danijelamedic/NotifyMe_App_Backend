package com.notifyme.isa.notifyme_backend.controller;

import com.notifyme.isa.notifyme_backend.messaging.UploadEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestPublishController {

    private final UploadEventPublisher publisher;

    public TestPublishController(UploadEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/publish")
    public String publish(@RequestParam String msg) {
        publisher.publish(msg);
        return "Sent: " + msg;
    }
}
