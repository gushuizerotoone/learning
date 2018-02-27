package com.gulewang.backend.session;

import com.gulewang.backend.config.RedisConfig;
import com.gulewang.common.exception.AppExceptionCode;
import com.gulewang.common.util.AssertUtil;
import com.gulewang.common.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thomas on 17/4/8.
 */
@Service
public class SessionService {

  @Autowired
  RedisTemplate<String, SessionInfo> sessionInfoRedisTemplate;

  public SessionInfo getSessionByAccessToken(HttpServletRequest request) {
    String accessToken = CookieUtil.getCookieValue(SessionConstant.ACCESS_TOKEN, request);
    AssertUtil.checkNotNull(accessToken, AppExceptionCode.USER_NOT_LOGIN);
    return sessionInfoRedisTemplate.opsForValue().get(RedisConfig.PREFIX_CORE_SESSION + accessToken);
  }

  public void insertSession(String userId, String userName, HttpServletRequest request, HttpServletResponse response) {
    // generate token
    String accessToken = TokenUtil.generateToken();

    // store session
    SessionInfo sessionInfo = new SessionInfo();
    sessionInfo.setUserName(userName);
    sessionInfo.setLastLoginTime(new Date().getTime());
    sessionInfo.setUserId(userId);

    sessionInfoRedisTemplate.opsForValue().set(RedisConfig.PREFIX_CORE_SESSION + accessToken, sessionInfo, 10, TimeUnit.MINUTES);
    CookieUtil.setCookieKeyValue(SessionConstant.ACCESS_TOKEN, accessToken, request, response);
  }

  public void removeSession(HttpServletRequest request) {
    String accessToken = CookieUtil.getCookieValue(SessionConstant.ACCESS_TOKEN, request);
    if (accessToken != null) {
      sessionInfoRedisTemplate.delete(RedisConfig.PREFIX_CORE_SESSION + accessToken);
    }
  }

  public void updateSessionTimeout(HttpServletRequest request) {
    String accessToken = CookieUtil.getCookieValue(SessionConstant.ACCESS_TOKEN, request);
    if (accessToken != null) {
      sessionInfoRedisTemplate.expire(RedisConfig.PREFIX_CORE_SESSION + accessToken, 10, TimeUnit.MINUTES);
    }
  }
}
