package com.gushui.learning.spring.reactor.domain;

/**
 * Created by Thomas on 2018/2/27.
 */
public class Notification {
  private Long id;
  private String name;
  private String email;
  private String mobile;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
}
