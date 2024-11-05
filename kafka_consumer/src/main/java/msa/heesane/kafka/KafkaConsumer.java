package msa.heesane.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  @KafkaListener(topics = "my-topic", groupId = "my-group-id")
  public void consume(String message) {
    System.out.println("Received message: " + message);
  }
}
