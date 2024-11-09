package msa.heesane.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import msa.heesane.avro_sample.TestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProducerComponent {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  public void create(String name, int age) {
    kafkaTemplate.send(
        topic,
        "testKey",
        new TestDTO(UUID.randomUUID().hashCode(),name, age,"address")
    );
  }
}
