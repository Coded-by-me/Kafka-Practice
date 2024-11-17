package msa.heesane.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class Controller {

  private final ProducerComponent component;

  @GetMapping("/test/{name}/{age}")
  public void test(@PathVariable("name") String name, @PathVariable("age") int age){
    component.create(name, age);
  }

}
