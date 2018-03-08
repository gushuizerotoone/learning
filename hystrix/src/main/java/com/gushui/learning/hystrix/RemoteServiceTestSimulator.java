package com.gushui.learning.hystrix;


public class RemoteServiceTestSimulator {

  private long wait;
  private String identifier;

  RemoteServiceTestSimulator(long wait) throws InterruptedException {
    this.wait = wait;
  }

  RemoteServiceTestSimulator(long wait, String identifier) throws InterruptedException {
    this.wait = wait;
    this.identifier = identifier;
  }

  String execute() throws InterruptedException {
    if (identifier != null) {
      System.out.println("\n\ntest >>>>>>>>>>>>>> " + identifier);
    }
    Thread.sleep(wait);
    System.out.println("\n\ntest finished >>>>>>>>>>>>>> " + identifier);
    return "Success";
  }
}
