package com.gushui.learning.hystrix;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Thomas on 2018/3/7.
 */
@Component("springClient")
public class SpringClient {

  @Value("${remoteservice.timeout}")
  private int remoteServiceDelay;

  @HystrixCircuitBreaker
  public String invokeRemoteServiceWithHystrix() throws InterruptedException {
    return new RemoteServiceTestSimulator(remoteServiceDelay, "t1").execute();
  }

  public String invokeRemoteServiceWithOutHystrix() throws InterruptedException {
    return new RemoteServiceTestSimulator(remoteServiceDelay, "t2").execute();
  }

}
