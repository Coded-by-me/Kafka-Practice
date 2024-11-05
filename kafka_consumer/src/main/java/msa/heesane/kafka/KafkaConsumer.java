package msa.heesane.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "my-topic", groupId = "my-group-id")
  public void consume(String message) {
    System.out.println("Received message: " + message);

    TestDTO testDTO = null;

    try{
      testDTO = objectMapper.readValue(message, TestDTO.class);
    } catch(Exception e){
      e.printStackTrace();
    }

    assert testDTO != null;
    System.out.println("Name: " + testDTO.getName());
    System.out.println("Age: " + testDTO.getAge());
  }
}
