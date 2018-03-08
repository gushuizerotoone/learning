package com.gushui.learning.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HystrixTimeoutManualTest {

  @Test
  public void givenInputBobAndDefaultSettings_whenCommandExecuted_thenReturnHelloBob() {
    assertThat(new CommandHelloWorld("Bob").execute(), equalTo("Hello Bob!"));
  }

  @Test
  public void givenSvcTimeoutOf100AndDefaultSettings_whenRemoteSvcExecuted_thenReturnSuccess()
          throws InterruptedException {
    HystrixCommand.Setter config = HystrixCommand
            .Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroup2"));

    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(100)).execute(),
            equalTo("Success"));
  }

  @Test(expected = HystrixRuntimeException.class)
  public void givenSvcTimeoutOf10000AndDefaultSettings__whenRemoteSvcExecuted_thenExpectHRE() throws InterruptedException {
    HystrixCommand.Setter config = HystrixCommand
            .Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest3")); // default default_executionTimeoutInMilliseconds = 1000
    new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(2_000)).execute();
  }

  @Test
  public void givenSvcTimeoutOf5000AndExecTimeoutOf10000_whenRemoteSvcExecuted_thenReturnSuccess()
          throws InterruptedException {

    HystrixCommand.Setter config = HystrixCommand
            .Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest4"));
    HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
    commandProperties.withExecutionTimeoutInMilliseconds(10_000);
    config.andCommandPropertiesDefaults(commandProperties);

    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
            equalTo("Success"));
  }

  @Test(expected = HystrixRuntimeException.class)
  public void givenSvcTimeoutOf15000AndExecTimeoutOf5000__whenExecuted_thenExpectHRE()
          throws InterruptedException {
    HystrixCommand.Setter config = HystrixCommand
            .Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest5"));
    HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
    commandProperties.withExecutionTimeoutInMilliseconds(5_000);
    config.andCommandPropertiesDefaults(commandProperties);
    new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(15_000)).execute();
  }

  @Test
  public void givenSvcTimeoutOf500AndExecTimeoutOf10000AndThreadPool__whenExecuted_thenReturnSuccess()
          throws InterruptedException {

    HystrixCommand.Setter config = HystrixCommand
            .Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupThreadPool"));
    HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
    commandProperties.withExecutionTimeoutInMilliseconds(10_000);
    config.andCommandPropertiesDefaults(commandProperties);
    config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
            .withMaxQueueSize(10)
            .withCoreSize(3)
            .withQueueSizeRejectionThreshold(10));

    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
            equalTo("Success"));
  }

  @Test
  public void givenCircuitBreakerSetup__whenRemoteSvcCmdExecuted_thenReturnSuccess()
          throws InterruptedException {

    HystrixCommand.Setter config = HystrixCommand
            .Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));
    HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
    properties.withExecutionTimeoutInMilliseconds(6000);
    properties.withCircuitBreakerSleepWindowInMilliseconds(4_000); // sleep window
    properties.withMetricsRollingStatisticalWindowInMilliseconds(130_000); // it's important, because stat is base in this time window, default is 10000
    properties.withCircuitBreakerRequestVolumeThreshold(5);
    properties.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
    properties.withCircuitBreakerEnabled(true);

    config.andCommandPropertiesDefaults(properties);

    config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
            .withMaxQueueSize(6)
            .withCoreSize(6)
            .withQueueSizeRejectionThreshold(6));

    assertThat(this.invokeRemoteService(config, 10_000, "1"), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000, "2"), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000, "3"), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000, "4"), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000, "5"), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000, "6"), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000, "7"), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000, "8"), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000, "9"), equalTo(null));
    assertThat(this.invokeRemoteService(config, 10_000, "10"), equalTo(null));

    Thread.sleep(10000);

//        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
//        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));

    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500, "v1")).execute(),
            equalTo("Success"));
    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500, "v2")).execute(),
            equalTo("Success"));
    assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500, "v3")).execute(),
            equalTo("Success"));
  }

  public String invokeRemoteService(HystrixCommand.Setter config, int timeout, String identifier)
          throws InterruptedException {
    String response = null;
    try {
      response = new RemoteServiceTestCommand(config,
              new RemoteServiceTestSimulator(timeout, identifier)).execute();
    } catch (HystrixRuntimeException ex) {
      System.out.println("ex = " + ex);
    }
    return response;
  }
}
