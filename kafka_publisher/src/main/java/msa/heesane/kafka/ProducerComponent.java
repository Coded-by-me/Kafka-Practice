package msa.heesane.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProducerComponent {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  public void create(){
    kafkaTemplate.send(topic,"testKey", "Hello, Kafka!");
  }
}
