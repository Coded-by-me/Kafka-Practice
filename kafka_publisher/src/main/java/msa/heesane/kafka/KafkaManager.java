package msa.heesane.kafka;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaManager {

  private final AdminClient adminClient;

  public KafkaManager(KafkaAdmin kafkaAdmin){
    adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
  }

  public void describeTopicConfigs() throws ExecutionException, InterruptedException {
    Collection<ConfigResource> resources = List.of(new ConfigResource(ConfigResource.Type.TOPIC,"my-topic"));

    DescribeConfigsResult result = adminClient.describeConfigs(resources);

    log.info("result = {}",result.all().get());
  }

}
