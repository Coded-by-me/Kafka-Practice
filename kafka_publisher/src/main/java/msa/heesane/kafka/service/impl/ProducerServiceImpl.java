package msa.heesane.kafka.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.heesane.avro_sample.TestDTO;
import msa.heesane.kafka.model.CreateTopicRequest;
import msa.heesane.kafka.service.ProducerService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.config.ConfigResource;
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

  private static final Set<String > ALLOWED_CONFIG_KEYS = new HashSet<>(Arrays.asList(
      "retention.ms",
      "cleanup.policy",
      "min.insync.replicas"
  ));

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

  @Override
  public Map<String,String> describeTopicConfig(String topic) {

    try(AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())){
      ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topic);

      DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(List.of(configResource));

      Config config = describeConfigsResult.all().get().get(configResource);

      return config.entries().stream()
          .collect(Collectors.toMap(ConfigEntry::name, ConfigEntry::value));

    } catch (Exception e) {
      log.info("error : {}", e.getMessage());
    }
    return null;
  }

  @Override
  public void alterTopicConfig(String topic, Map<String, String> configs) {
    try(AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())){

      ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topic);

      // 설정 키가 허용된 키인지 확인
      if(!ALLOWED_CONFIG_KEYS.containsAll(configs.keySet())){
        throw new IllegalArgumentException("Not allowed config key");
      }

      // AlterConfigOp 리스트 생성
      List<AlterConfigOp> configOps = configs.entrySet().stream()
          .map(entry -> new AlterConfigOp(
              new ConfigEntry(entry.getKey(), entry.getValue()),
              AlterConfigOp.OpType.SET // 설정 추가/수정
          ))
          .toList();
      adminClient.incrementalAlterConfigs(Collections.singletonMap(configResource, configOps)).all().get();
    }catch (Exception e){
      log.info("error : {}", e.getMessage());
    }
  }
}
