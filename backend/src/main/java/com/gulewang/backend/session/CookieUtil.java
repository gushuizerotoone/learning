package com.gulewang.backend.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Thomas on 17/4/8.
 */
public class CookieUtil {

  public static void setCookieKeyValue(String key, String value, HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      Arrays.stream(cookies).filter(cookie1 -> key.equals(cookie1.getName())).forEach(cookie1 -> cookie1.setMaxAge(0));
    }

    Cookie cookie = new Cookie(key, value);
    cookie.setPath("/"); // important, 否则path是/user, 去拿/stock等就不会送上这个cookie了
    response.addCookie(cookie);
  }

  public static String getCookieValue(String key, HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }
    Optional<Cookie> a = Arrays.stream(cookies).filter(cookie1 -> key.equals(cookie1.getName())).findAny();
    if (a.isPresent()) {
      return a.get().getValue();
    } else {
      return null;
    }
  }
}

