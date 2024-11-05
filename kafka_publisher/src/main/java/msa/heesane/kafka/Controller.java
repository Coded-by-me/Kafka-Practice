package msa.heesane.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class Controller {

  private final ProducerComponent component;

  @GetMapping("/test")
  public void test(){
    component.create();
  }

}
