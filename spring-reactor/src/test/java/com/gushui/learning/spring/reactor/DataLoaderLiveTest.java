package com.gushui.learning.spring.reactor;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class DataLoaderLiveTest {

  @Test
  public void exampleTest() {
    RestTemplate restTemplate = new RestTemplate();
    String resp = restTemplate.getForObject("http://localhost:8080/startNotification/10", String.class);
    System.out.println("resp: " + resp);
  }

}
