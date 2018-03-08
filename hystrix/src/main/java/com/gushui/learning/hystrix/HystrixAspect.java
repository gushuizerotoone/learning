package com.gushui.learning.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPool;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Thomas on 2018/3/7.
 */
@Component
@Aspect
public class HystrixAspect {
  private HystrixCommand.Setter config;
  private HystrixCommandProperties.Setter commandProperties;
  private HystrixThreadPoolProperties.Setter threadPoolProperties;

  @Value("${remoteservice.command.execution.timeout}")
  private int executionTimeout;

  @Value("${remoteservice.command.sleepwindow}")
  private int sleepWindow;

  @Value("${remoteservice.command.threadpool.maxsize}")
  private int maxThreadCount;

  @Value("${remoteservice.command.threadpool.coresize}")
  private int coreThreadCount;

  @Value("${remoteservice.command.task.queue.size}")
  private int queueCount;

  @Value("${remoteservice.command.group.key}")
  private String groupKey;

  @Value("${remoteservice.command.key}")
  private String key;

  @Around("@annotation(com.gushui.learning.hystrix.HystrixCircuitBreaker)")
  public Object circuitBreakAround(final ProceedingJoinPoint proceedingJoinPoint) {
    return new RemoteServiceCommand(config, proceedingJoinPoint).execute();
  }


  @PostConstruct
  public void setup() {
    this.config = HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey));
    this.config = config.andCommandKey(HystrixCommandKey.Factory.asKey(key));

    this.commandProperties = HystrixCommandProperties.Setter();
    this.commandProperties.withCircuitBreakerSleepWindowInMilliseconds(sleepWindow);
    this.commandProperties.withExecutionTimeoutInMilliseconds(executionTimeout);

    this.threadPoolProperties = HystrixThreadPoolProperties.Setter();
    this.threadPoolProperties.withCoreSize(coreThreadCount).withMaxQueueSize(maxThreadCount).withQueueSizeRejectionThreshold(queueCount);

    this.config.andCommandPropertiesDefaults(commandProperties);
    this.config.andThreadPoolPropertiesDefaults(threadPoolProperties);
  }

  public static class RemoteServiceCommand extends HystrixCommand<String> {

    private ProceedingJoinPoint joinPoint;

    public RemoteServiceCommand(Setter config, ProceedingJoinPoint joinPoint) {
      super(config);
      this.joinPoint = joinPoint;
    }

    @Override
    protected String run() throws Exception {
      try {
        return (String) joinPoint.proceed();
      } catch (Throwable throwable) {
        throw new Exception(throwable);
      }
    }
  }

}
