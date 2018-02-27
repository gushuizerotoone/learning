package com.gulewang.backend.rabbitMq.message;

/**
 * Created by Thomas on 2017/4/15.
 */
public class UserRegisterMessage {
  private String userId;

  public UserRegisterMessage() {
  }

  public UserRegisterMessage(String userId) {
    this.userId = userId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
