package msa.heesane.kafka.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import msa.heesane.kafka.model.CreateTopicRequest;
import msa.heesane.kafka.service.ProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class KafkaController {

  private final ProducerService producerService;

  // 간단한 메세지를 전달 -> Avro를 통해서 Broker로 전달하는 간단한 예제
  @GetMapping("/send")
  public void sendWithAvro(@RequestParam("name") String name, @RequestParam("age") int age){
    producerService.sendWithAvro(name, age);
  }

  // Kafka Cluster에 등록된 모든 Topic 목록을 조회한다.
  // Internal Topic에 대한 정보도 함께 출력된다. (Internal Topic은 __로 시작하는 Topic)
  @GetMapping("/list")
  public ResponseEntity<Set<String>> listTopics(){
    return ResponseEntity.ok(producerService.listTopics());
  }

  // Topic을 생성한다.
  // Topic 생성 시, Partition 수와 Replication Factor를 지정할 수 있다.
  // Partition 수와 Replication Factor를 지정하지 않을 경우, 기본값이 적용된다.
  @PostMapping("/create")
  public void createTopic(@RequestBody CreateTopicRequest request){
    producerService.createTopic(request);
  }

  @DeleteMapping("/delete")
  public void deleteTopic(@RequestParam("topic") String topic){
    producerService.deleteTopic(topic);
  }

  @GetMapping("/describe")
  public ResponseEntity<Map<String, Object>> describeTopic(@RequestParam("topic") String topic){
    return ResponseEntity.ok(producerService.describeTopic(topic));
  }

  @GetMapping("/describe/config")
  public ResponseEntity<Map<String,String>> describeTopicConfig(@RequestParam("topic") String topic){
    return ResponseEntity.ok(producerService.describeTopicConfig(topic));
  }

  @PostMapping("/update/config")
  public void updateTopicConfig(@RequestParam("topic") String topic, @RequestBody Map<String,String> config){
    producerService.alterTopicConfig(topic, config);
  }

  @GetMapping("/list/consumer")
  public ResponseEntity<Map<String, Object>> listConsumerGroups(){
    return ResponseEntity.ok(producerService.listConsumerGroups());
  }

  @GetMapping("/describe/consumer")
  public ResponseEntity<Map<String,Object>> describeConsumerGroup(@RequestParam("groupId") String groupId){
    return ResponseEntity.ok(producerService.describeConsumerGroup(groupId));
  }

  @GetMapping("/list/consumer/offsets")
  public ResponseEntity<Map<String, Object>> listConsumerGroupOffsets(@RequestParam("groupId") String groupId){
    return ResponseEntity.ok(producerService.listConsumerGroupOffsets(groupId));
  }

  @GetMapping("/list/consumer/offsets/all")
  public ResponseEntity<Map<String, Map<String, List<Map<String, Object>>>>> listAllConsumerGroupOffsets(){
    return ResponseEntity.ok(producerService.listAllConsumerGroupOffsets());
  }
}
