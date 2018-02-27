package com.gulewang.backend.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Thomas on 17/4/7.
 */
@ApiModel(value = "用户注册请求")
public class RegisterUserReqBo {
  @ApiModelProperty(value = "用户名", required = true)
  private String userName;

  @ApiModelProperty(value = "密码", required = true)
  private String password;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
