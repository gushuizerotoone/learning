package com.gulewang.backend.session;

/**
 * Created by Thomas on 17/4/7.
 */
public class SessionInfo {
  private String userId;
  private String userName;
  private Long lastLoginTime;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Long getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Long lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }
}
