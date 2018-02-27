package com.gulewang.backend.service;

import com.gulewang.article.service.ImageService;
import com.gulewang.backend.bean.AggregateUserBo;
import com.gulewang.backend.bean.RegisterUserReqBo;
import com.gulewang.backend.rabbitMq.SenderAndReceiver.UserTopicSender;
import com.gulewang.backend.rabbitMq.message.UserRegisterMessage;
import com.gulewang.backend.session.SessionService;
import com.gulewang.common.exception.AppExceptionCode;
import com.gulewang.common.util.AssertUtil;
import com.gulewang.stock.service.UserStockService;
import com.gulewang.user.bean.CreateUserReqBo;
import com.gulewang.user.bean.UserBo;
import com.gulewang.user.service.UserService;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

/**
 * Created by Thomas on 17/4/7.
 */
@Service
public class CompositeUserServiceImpl implements CompositeUserService {

  private static final Logger logger = LoggerFactory.getLogger(CompositeUserServiceImpl.class);

  @Autowired
  UserService userService;

  @Autowired
  RedisTemplate redisTemplate;

  @Autowired
  SessionService sessionService;

  @Autowired
  UserTopicSender userTopicSender;

  @Autowired
  UserStockService userStockService;

  @Autowired
  ImageService imageService;

  @Override
  public AggregateUserBo login(String userName, String password, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    sessionService.removeSession(httpServletRequest);

    UserBo userBo = userService.login(userName, password);
    AssertUtil.checkNotNull(userBo, AppExceptionCode.USERNAME_PASSWORD_ERR);

    sessionService.insertSession(userBo.getId(), userName, httpServletRequest, httpServletResponse);

    AggregateUserBo aggregateUserBo = convertToAggregateUserBo.apply(userBo);

    logger.info("Response to login, aggregateUserBo: {}", aggregateUserBo);
    return aggregateUserBo;
  }

  @Override
  public String registerUser(RegisterUserReqBo registerUserReqBo, HttpServletRequest request, HttpServletResponse response) {
    sessionService.removeSession(request);

    CreateUserReqBo createUserReqBo = convertToCreateUserReqBo.apply(registerUserReqBo);
    String userId = userService.createUser(createUserReqBo);

    // public register success event
    userTopicSender.publishRegisterEvent(new UserRegisterMessage(userId));
    sessionService.insertSession(userId, createUserReqBo.getUserName(), request, response);

    return userId;
  }

  @Override
  public boolean logout(HttpServletRequest httpServletRequest) {
    sessionService.removeSession(httpServletRequest);
    return true;
  }

  @Override
  public AggregateUserBo getUser(String userId) {
    logger.info("Request to get user: {}", userId);
    UserBo userBo = userService.getUser(userId);
    AggregateUserBo aggregateUserBo = convertToAggregateUserBo.apply(userBo);

    logger.info("Response to get user, aggregateUserBo: {}", aggregateUserBo);
    return aggregateUserBo;
  }

  @Override
  public AggregateUserBo getUserByUserName(String userName) {
    logger.info("Request to get user: {}", userName);
    UserBo userBo = userService.getUserByUserName(userName);
    AggregateUserBo aggregateUserBo = convertToAggregateUserBo.apply(userBo);

    logger.info("Response to get user, aggregateUserBo: {}", aggregateUserBo);
    return aggregateUserBo;
  }

  Function<RegisterUserReqBo, CreateUserReqBo> convertToCreateUserReqBo = registerUserReqBo -> {
    CreateUserReqBo createUserReqBo = new CreateUserReqBo();
    createUserReqBo.setPassword(registerUserReqBo.getPassword());
    createUserReqBo.setUserName(registerUserReqBo.getUserName());
    createUserReqBo.setNickName(registerUserReqBo.getUserName());
    createUserReqBo.setProfileImage(imageService.getDefaultProfileImage());
    return createUserReqBo;
  };

  Function<UserBo, AggregateUserBo> convertToAggregateUserBo = userBo -> {
    AggregateUserBo aggregateUserBo = new AggregateUserBo();
    try {
      PropertyUtils.copyProperties(aggregateUserBo, userBo);
    } catch (Exception e) {
      logger.warn("Catch Exception while convertToAggregateUserBo" + userBo, e);
    }

    aggregateUserBo.setFriendsCount(userBo.getFriendUserIds().size());
    aggregateUserBo.setStocksCount(userStockService.getUserInterestStocks(userBo.getId()).getCodes().size());
    aggregateUserBo.setAiValue("30"); // TODO: thomas, fill with the real value in future.

    return aggregateUserBo;
  };
}
