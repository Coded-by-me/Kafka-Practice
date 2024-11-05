package msa.heesane.kafka;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Slf4j
@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.producer.key-serializer}")
  private String keySerializer;

  @Value("${spring.kafka.producer.value-serializer}")
  private String valueSerializer;

  @Bean
  public ProducerFactory<String, Object> producerFactory() {

    log.info("bootstrapServers: {}", bootstrapServers);
    log.info("keySerializer: {}", keySerializer);
    log.info("valueSerializer: {}", valueSerializer);

    Map<String, Object> config = new HashMap<>();
    config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
    config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);

    return new DefaultKafkaProducerFactory<>(config);
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
