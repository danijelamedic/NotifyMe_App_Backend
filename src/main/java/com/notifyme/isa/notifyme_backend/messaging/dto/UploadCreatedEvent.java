package com.notifyme.isa.notifyme_backend.messaging.dto;

import java.time.Instant;

public class UploadCreatedEvent {
    private Long videoId;
    private String authorUsername;
    private Instant createdAt;

    public UploadCreatedEvent() {}

    public UploadCreatedEvent(Long videoId, String authorUsername, Instant createdAt) {
        this.videoId = videoId;
        this.authorUsername = authorUsername;
        this.createdAt = createdAt;
    }

    public Long getVideoId() { return videoId; }
    public void setVideoId(Long videoId) { this.videoId = videoId; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
