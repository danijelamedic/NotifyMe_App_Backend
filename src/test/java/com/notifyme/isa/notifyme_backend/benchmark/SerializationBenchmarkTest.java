package com.notifyme.isa.notifyme_backend.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.notifyme.isa.notifyme_backend.messaging.dto.UploadCreatedEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

//JUNIT test
class SerializationBenchmarkTest {

    private static final int N = 50;
    private static final int WARMUP = 10;
    private static volatile Object SINK;


    @Test
    void compareJsonVsProtobuf_50messages() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // 1) napravi 50 JSON DTO eventova
        Instant ts = Instant.now();
        List<UploadCreatedEvent> jsonEvents = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            jsonEvents.add(new UploadCreatedEvent(
                    (long) (100 + i),
                    "author" + i,
                    ts,
                    "Video " + i,
                    123456L + i
            ));
        }


        // 2) napravi 50 Protobuf eventova
        List<notifyme.upload.proto.UploadCreatedEvent> pbEvents = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            UploadCreatedEvent dto = jsonEvents.get(i);

            notifyme.upload.proto.UploadCreatedEvent pb =
                    notifyme.upload.proto.UploadCreatedEvent.newBuilder()
                            .setVideoId(dto.getVideoId())
                            .setTitle(dto.getTitle())
                            .setSizeBytes(dto.getSizeBytes())
                            .setAuthorUsername(dto.getAuthorUsername())
                            .setCreatedAt(dto.getCreatedAt().toString())
                            .build();

            pbEvents.add(pb);
        }

        // 3) Warmup
        for (int i = 0; i < WARMUP; i++) {
            byte[] b1 = objectMapper.writeValueAsBytes(jsonEvents.get(i % N));
            SINK = objectMapper.readValue(b1, UploadCreatedEvent.class);

            byte[] b2 = pbEvents.get(i % N).toByteArray();
            SINK = notifyme.upload.proto.UploadCreatedEvent.parseFrom(b2);
        }


        // 4) Merenje JSON
        long jsonSerTotalNs = 0, jsonDeserTotalNs = 0, jsonSizeTotal = 0;

        for (int i = 0; i < N; i++) {
            UploadCreatedEvent dto = jsonEvents.get(i);

            long t1 = System.nanoTime();
            byte[] bytes = objectMapper.writeValueAsBytes(dto);
            long t2 = System.nanoTime();

            long t3 = System.nanoTime();
            SINK = objectMapper.readValue(bytes, UploadCreatedEvent.class);
            long t4 = System.nanoTime();

            jsonSerTotalNs += (t2 - t1);
            jsonDeserTotalNs += (t4 - t3);
            jsonSizeTotal += bytes.length;
        }

        // 5) Merenje Protobuf
        long pbSerTotalNs = 0, pbDeserTotalNs = 0, pbSizeTotal = 0;

        for (int i = 0; i < N; i++) {
            notifyme.upload.proto.UploadCreatedEvent pb = pbEvents.get(i);

            long t1 = System.nanoTime();
            byte[] bytes = pb.toByteArray();
            long t2 = System.nanoTime();

            long t3 = System.nanoTime();
            SINK = notifyme.upload.proto.UploadCreatedEvent.parseFrom(bytes);
            long t4 = System.nanoTime();

            pbSerTotalNs += (t2 - t1);
            pbDeserTotalNs += (t4 - t3);
            pbSizeTotal += bytes.length;
        }

        double jsonSerAvg = jsonSerTotalNs / (double) N;
        double jsonDeserAvg = jsonDeserTotalNs / (double) N;
        double jsonSizeAvg = jsonSizeTotal / (double) N;

        double pbSerAvg = pbSerTotalNs / (double) N;
        double pbDeserAvg = pbDeserTotalNs / (double) N;
        double pbSizeAvg = pbSizeTotal / (double) N;

        System.out.println("=== JSON ===");
        System.out.println("avg serialize (ns): " + jsonSerAvg);
        System.out.println("avg deserialize (ns): " + jsonDeserAvg);
        System.out.println("avg size (bytes): " + jsonSizeAvg);

        System.out.println("=== PROTOBUF ===");
        System.out.println("avg serialize (ns): " + pbSerAvg);
        System.out.println("avg deserialize (ns): " + pbDeserAvg);
        System.out.println("avg size (bytes): " + pbSizeAvg);
    }
}
