package com.gulewang.common.util;


import com.gulewang.common.exception.AppException;
import com.gulewang.common.exception.AppExceptionCode;

/**
 * Created by Thomas on 16/12/1.
 */
public class AssertUtil {

  public static void checkNotNull(Object object, AppExceptionCode exceptionCode) {
    if (object == null) {
      throw new AppException(exceptionCode);
    }
  }

  public static void checkNotNull(Object object, AppExceptionCode exceptionCode, String additionalDesc) {
    if (object == null) {
      throw new AppException(exceptionCode, additionalDesc);
    }
  }

  public static void checkState(boolean condition, AppExceptionCode exceptionCode) {
    if (!condition) {
      throw new AppException(exceptionCode);
    }
  }

  public static void checkState(boolean condition, AppExceptionCode exceptionCode, String additionalDesc) {
    if (!condition) {
      throw new AppException(exceptionCode, additionalDesc);
    }
  }

}
