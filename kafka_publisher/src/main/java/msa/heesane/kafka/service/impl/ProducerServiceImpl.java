package msa.heesane.kafka.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.heesane.avro_sample.TestDTO;
import msa.heesane.kafka.model.CreateTopicRequest;
import msa.heesane.kafka.service.ProducerService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  private final KafkaAdmin kafkaAdmin;

  private final ObjectMapper objectMapper;

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  @Override
  public void sendWithObjectMapper(String name, int age) {
    try{
      kafkaTemplate.send(
          topic,
          "testKey",
          objectMapper.writeValueAsString(new TestDTO(UUID.randomUUID().hashCode(),name, age,"address"))
      );
    }catch(Exception e){
      log.info("error : {}", e.getMessage());
    }

  }

  @Override
  public void sendWithAvro(String name, int age) {
    kafkaTemplate.send(
        topic,
        "testKey",
        new TestDTO(UUID.randomUUID().hashCode(),name, age,"address")
    );
  }

  @Override
  public void createTopic(CreateTopicRequest request) {
    try(AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())){
      adminClient.createTopics(
          List.of(new NewTopic(request.getTopic(), request.getPartitions(), request.getReplicationFactor()))
      );
    } catch(Exception e){
      log.info("error : {}", e.getMessage());
    }
  }

  @Override
  public Set<String> listTopics() {
    try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
      ListTopicsResult topics = adminClient.listTopics();
      return topics.names().get(); // 토픽 이름 리스트 반환
    } catch (InterruptedException | ExecutionException e) {
      log.info("error : {}", e.getMessage());
    }
    return Collections.emptySet();
  }

  @Override
  public void deleteTopic(String topic) {
    try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
      adminClient.deleteTopics(List.of(topic));
    } catch (Exception e) {
      log.info("error : {}", e.getMessage());
    }
  }

  @Override
  public Map<String, Object> describeTopic(String topic) {
    try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
      DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(List.of(topic));
      TopicDescription description = describeTopicsResult.topicNameValues().get(topic).get();

      return Map.of(
          "name", description.name(),
          "isInternal", description.isInternal(),
          "partitions", description.partitions().stream().map(partitionInfo -> Map.of(
              "partition", partitionInfo.partition(),
              "leader", partitionInfo.leader().id(),
              "replicas", partitionInfo.replicas().stream().map(Node::id).toList(),
              "isr", partitionInfo.isr().stream().map(Node::id).toList()
          )).toList()
      );

    } catch (Exception e) {
      log.info("error : {}", e.getMessage());
    }
    return Collections.emptyMap();
  }
}
