/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gulewang.backend.rabbitMq;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  @Bean
  public TopicExchange topic() {
    return new TopicExchange(RabbitMqConstants.EXCHANGE_STOCK_TOPIC);
  }

  @Bean
  public Queue userQueue() {
    return new AnonymousQueue();
  }

  @Bean
  public Binding bindingUserRegister(TopicExchange topic, Queue userQueue) {
    return BindingBuilder.bind(userQueue).to(topic).with(RabbitMqConstants.TOPIC_STOCK_USER_REGISTER_SUCCESS);
  }

}
