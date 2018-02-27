package com.gulewang.backend.service;

import com.gulewang.backend.bean.AggregateUserBo;
import com.gulewang.backend.bean.RegisterUserReqBo;
import com.gulewang.user.bean.UserBo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Thomas on 17/4/7.
 */
public interface CompositeUserService {

  AggregateUserBo login(String userName, String password, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

  String registerUser(RegisterUserReqBo registerUserReqBo, HttpServletRequest request, HttpServletResponse response);

  boolean logout(HttpServletRequest httpServletRequest);

  AggregateUserBo getUser(String userId);

  AggregateUserBo getUserByUserName(String userName);
}
