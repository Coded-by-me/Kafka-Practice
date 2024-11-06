package msa.heesane.kafka;

import java.util.Collection;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

public class TestHandleConsumer implements ConsumerRebalanceListener {

  // 컨슈머가 할당 받았던 파티션이 해제된때, -> 리밸런싱이나 컨슈머가 종료되었을때 호출
  // onPartitionsLost() 메소드는 2.8.0 버전부터 사용 가능
  // 동일한 기능임.
  @Override
  public void onPartitionsRevoked(Collection<TopicPartition> collection) {

  }

  // 컨슈머가 새로운 파티션을 할당 받았을때 호출
  @Override
  public void onPartitionsAssigned(Collection<TopicPartition> collection) {

  }

  @Override
  public void onPartitionsLost(Collection<TopicPartition> collection) {

  }

}
