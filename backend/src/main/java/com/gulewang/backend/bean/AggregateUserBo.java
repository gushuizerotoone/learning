package com.gulewang.backend.bean;

import com.gulewang.user.bean.UserBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Thomas on 2017/6/26.
 */
@ApiModel(value = "用户信息")
public class AggregateUserBo extends UserBo {

  @ApiModelProperty(value = "好友数目")
  private Integer friendsCount;

  @ApiModelProperty(value = "关注公司数")
  private Integer stocksCount;

  @ApiModelProperty(value = "AI值")
  private String aiValue;

  public Integer getFriendsCount() {
    return friendsCount;
  }

  public void setFriendsCount(Integer friendsCount) {
    this.friendsCount = friendsCount;
  }

  public String getAiValue() {
    return aiValue;
  }

  public void setAiValue(String aiValue) {
    this.aiValue = aiValue;
  }

  public Integer getStocksCount() {
    return stocksCount;
  }

  public void setStocksCount(Integer stocksCount) {
    this.stocksCount = stocksCount;
  }
}
