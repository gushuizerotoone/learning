package com.gushui.learning.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * Created by Thomas on 2018/3/7.
 */
public class CommandHelloWorld extends HystrixCommand<String> {
  private String name;

  public CommandHelloWorld(String name) {
    super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
    this.name = name;
  }

  @Override
  protected String run() throws Exception {
    return "Hello " + name + "!";
  }
}
