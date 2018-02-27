package com.gulewang.backend.rabbitMq;

/**
 * Created by Thomas on 2017/4/15.
 */
public class RabbitMqConstants {
  public static final String EXCHANGE_STOCK_TOPIC = "stock.topic";

  /** format: stock.module.function.status */
  public static final String TOPIC_STOCK_USER_REGISTER_SUCCESS = "stock.user.register.success";

}
