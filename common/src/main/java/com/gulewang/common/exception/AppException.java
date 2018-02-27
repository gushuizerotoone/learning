package com.gulewang.common.exception;

public class AppException extends RuntimeException {

  private final AppExceptionCode errorCode;

  private String additionalDesc;

  public AppException(AppExceptionCode errorCode) {
    super(errorCode.toString());
    this.errorCode = errorCode;
  }

  public AppException(AppExceptionCode errorCode, String additionalDesc) {
    super(errorCode.toString());
    this.errorCode = errorCode;
    this.additionalDesc = additionalDesc;
  }

  public String getAdditionalDesc() {
    return additionalDesc;
  }

  public void setAdditionalDesc(String additionalDesc) {
    this.additionalDesc = additionalDesc;
  }

  public AppExceptionCode getErrorCode() {
    return errorCode;
  }

}
