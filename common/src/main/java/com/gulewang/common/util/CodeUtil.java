package com.gulewang.common.util;

/**
 * Created by Thomas on 2017/6/10.
 */
public class CodeUtil {

  /**
   * 模糊
   * 300 -> SZ300
   * @param code
   * @return
   */
  public static String getQueryCode(String code) {
    code = code.toUpperCase();
    if (code.startsWith("0") || code.startsWith("3") || code.startsWith("2")) {
      code = "SZ" + code;
    } else if (code.startsWith("6") || code.startsWith("9") || code.startsWith("7")) {
      code = "SH" + code;
    }
    return code;
  }

}
