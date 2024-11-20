package msa.heesane.kafka.service;

import java.util.Map;
import java.util.Set;
import msa.heesane.kafka.model.CreateTopicRequest;
import org.apache.kafka.clients.admin.Config;

public interface ProducerService {

  void sendWithObjectMapper(String name, int age);

  void sendWithAvro(String name, int age);

  void createTopic(CreateTopicRequest request);

  Set<String> listTopics();

  void deleteTopic(String topic);

  Map<String, Object> describeTopic(String topic);

  Map<String,String> describeTopicConfig(String topic);
}
