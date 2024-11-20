package msa.heesane.kafka.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CreateTopicRequest {

  @JsonProperty
  private String topic;

  @JsonProperty
  private int partitions;

  @JsonProperty
  private short replicationFactor;

  @JsonCreator
  public CreateTopicRequest(String topic, int partitions, short replicationFactor) {
    this.topic = topic;
    this.partitions = partitions;
    this.replicationFactor = replicationFactor;
  }
}
