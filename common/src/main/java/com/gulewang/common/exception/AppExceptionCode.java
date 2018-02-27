package com.gulewang.common.exception;

public enum AppExceptionCode {
  ILLEGAL_RESOURCE_REQUEST("100006"),
  USERNAME_PASSWORD_ERR("100006"), // 用户名和密码错误
  USER_NOT_LOGIN("100006"), // 请登录
  STOCK_CODE_NOT_VALID("100006"),
  ILLEGAL_ARGUMENT("100007"),
  PERMISSION_DENY("100008"),
  ILLEGAL_IMAGE_TYPE("100009"), // 现在只支持JPG, PNG
  UPLOAD_IMAGE_FAIL("100010"), // 上传图片出错
  DOWNLOAD_IMAGE_FAIL("100011"), // 上传图片出错
  USER_NOT_FOUND("100012"), // 找不到用户


  INTERNAL_ERROR("999999"),
  ;

  private final String code;

  AppExceptionCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return String.format("%s[%s]", this.name(), this.code);
  }

}
