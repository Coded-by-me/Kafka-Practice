package msa.heesane.kafka;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.heesane.avro_sample.TestDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

  @KafkaListener(topics = "my-topic")
  public void consume(
      TestDTO message,
      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(KafkaHeaders.OFFSET) long offset,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {


    log.info("-------MESSAGE----------------");
    log.info("Received Payload: {}", message);
    log.info("Received Partition: " + partition);
    log.info("Received Offset: " + offset);
    log.info("Received Timestamp: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
        ZoneId.of("Asia/Seoul")));
    log.info("-------------------------------");
  }
}
