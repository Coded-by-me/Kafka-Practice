package msa.heesane.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProducerComponent {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  public void create(String name, int age) throws JsonProcessingException {


    kafkaTemplate.send(topic,"testKey", objectMapper.writeValueAsString(new TestDTO(name, age)));
  }
}
