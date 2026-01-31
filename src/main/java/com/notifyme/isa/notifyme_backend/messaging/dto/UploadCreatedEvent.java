package com.notifyme.isa.notifyme_backend.messaging.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.*;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadCreatedEvent {

    private Long videoId;
    private String title;
    private String authorUsername;

    @JsonAlias({"videoSizeBytes", "sizeBytes"})
    private Long sizeBytes;

    @JsonAlias({"thumbnailSizeBytes"})
    private Long thumbnailSizeBytes;

    private Instant createdAt;

    public Long getVideoId() { return videoId; }
    public void setVideoId(Long videoId) { this.videoId = videoId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public Long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }

    public Long getThumbnailSizeBytes() { return thumbnailSizeBytes; }
    public void setThumbnailSizeBytes(Long thumbnailSizeBytes) { this.thumbnailSizeBytes = thumbnailSizeBytes; }

    public Instant getCreatedAt() { return createdAt; }

    @JsonSetter("createdAt")
    public void setCreatedAtFlexible(Object v) {
        if (v == null) {
            this.createdAt = null;
            return;
        }
        if (v instanceof Number n) {
            long val = n.longValue();
            this.createdAt = Instant.ofEpochMilli(val);
            return;
        }
        if (v instanceof String s) {
            this.createdAt = Instant.parse(s);
            return;
        }
        if (v instanceof List<?> a && a.size() >= 6) {
            int year = ((Number) a.get(0)).intValue();
            int month = ((Number) a.get(1)).intValue();
            int day = ((Number) a.get(2)).intValue();
            int hour = ((Number) a.get(3)).intValue();
            int minute = ((Number) a.get(4)).intValue();
            int second = ((Number) a.get(5)).intValue();
            int nano = (a.size() >= 7) ? ((Number) a.get(6)).intValue() : 0;

            LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute, second, nano);
            this.createdAt = ldt.atZone(ZoneId.systemDefault()).toInstant();
            return;
        }

        throw new IllegalArgumentException("Unsupported createdAt format: " + v);
    }
}
