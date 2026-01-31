# NotifyMe Backend  
**MQ JSON vs Protobuf (ISA – tačka 3.14)**

NotifyMe je Spring Boot aplikacija koja prima poruke svaki put kada se objavi novi video na sistemu **Jutjubić**, koristeći **RabbitMQ** kao message queue.

U okviru zadatka **3.14 – MQ JSON vs Protobuf**, implementirano je slanje poruka u dva formata:
- **JSON** (Jackson)
- **Protocol Buffers (Protobuf)**

Takođe je implementirano **poređenje ova dva formata** u pogledu:
- prosečnog vremena serijalizacije
- prosečnog vremena deserijalizacije
- prosečne veličine poruke  

Poređenje je izvršeno na **50 poruka**, kako zahteva specifikacija.

---

## Tehnologije
- Java 17  
- Spring Boot  
- RabbitMQ (Docker)  
- Jackson (JSON)  
- Protobuf (proto3 + Maven plugin)  
- JUnit 5  

---

## UploadEvent struktura

Informacije koje se šalju u poruci:
- `videoId`
- `title`
- `sizeBytes`
- `authorUsername`
- `createdAt`

Postoje dve reprezentacije:
- **JSON DTO**: `UploadCreatedEvent`
- **Protobuf message**: `notifyme.upload.proto.UploadCreatedEvent`

---

## RabbitMQ konfiguracija

### Exchange
- `jutjubic.upload` (type: topic)

### JSON queue
- Queue: `notifyme.upload.events.v2`
- Routing key: `upload.created`

### Protobuf queue
- Queue: `notifyme.upload.events.pb`
- Routing key: `upload.created.pb`

### Dead Letter setup
- DLX: `notifyme.upload.dlx`
- DLQ: `notifyme.upload.events.dlq`
- DLQ routing key: `upload.created.dlq`

Sve deklaracije (exchange, queue, binding, DLQ) se kreiraju automatski prilikom pokretanja aplikacije.

---

## Protobuf schema

Proto fajl se nalazi u:
src/main/proto/upload_event.proto

Generisane klase se nalaze u:
target/generated-sources/protobuf/java


---

## Pokretanje RabbitMQ (Docker)

```
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3-management

```
RabbitMQ Management UI:
```
http://localhost:15672
```
```
user: guest

password: guest

```
## Pokretanje aplikacije

Windows
```
.\mvnw.cmd clean spring-boot:run
```

Linux / macOS
```
./mvnw clean spring-boot:run
```

## Benchmark test (JSON vs Protobuf)

Benchmark test je implementiran pomoću JUnit 5.

Test fajl:

src/test/java/com/notifyme/isa/notifyme_backend/benchmark/SerializationBenchmarkTest.java


Pokretanje testa:

Windows
```
.\mvnw.cmd test -Dtest=SerializationBenchmarkTest
```

Linux / macOS
```
./mvnw test -Dtest=SerializationBenchmarkTest
```

Test meri:

- prosečno vreme serijalizacije (ns)
- prosečno vreme deserijalizacije (ns)
- prosečnu veličinu poruke (bytes)


## Rezultati benchmark testa (50 poruka)<br>
```
=== JSON ===<br>
avg serialize (ns): 128174.0<br>
avg deserialize (ns): 190766.0<br>
avg size (bytes): 113.6<br>
<br><br>
=== PROTOBUF ===<br>
avg serialize (ns): 21208.0<br>
avg deserialize (ns): 14230.0<br>
avg size (bytes): 58.04<br>
```
Zaključak
- Protobuf ima značajno bržu serijalizaciju i deserijalizaciju
- Protobuf poruke su oko 2x manje od JSON poruka
- Rezultati potvrđuju efikasnost Protobuf formata za message queue komunikaciju
