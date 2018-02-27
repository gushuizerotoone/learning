package com.gulewang.common.util;

import java.util.UUID;

/**
 * Created by Thomas on 17/4/7.
 */
public class TokenUtil {

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }
}
