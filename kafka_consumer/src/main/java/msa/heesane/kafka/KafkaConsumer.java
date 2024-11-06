package msa.heesane.kafka;

import com.example.avro.TestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "my-topic", groupId = "my-group-id")
  public void consume(
      String message,
      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(KafkaHeaders.OFFSET) long offset,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {

    TestDTO testDTO = null;

    try {
      testDTO = objectMapper.readValue(message, TestDTO.class);
    } catch (Exception e) {
      e.printStackTrace();
    }

    assert testDTO != null;

    log.info("-------MESSAGE----------------");
    log.info("Received Payload: {}", testDTO);
    log.info("Received Partition: " + partition);
    log.info("Received Offset: " + offset);
    log.info("Received Timestamp: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("Asia/Seoul")));
    log.info("-------------------------------");
  }
}
