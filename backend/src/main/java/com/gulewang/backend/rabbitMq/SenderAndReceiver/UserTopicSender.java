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
package com.gulewang.backend.rabbitMq.SenderAndReceiver;

import com.gulewang.backend.rabbitMq.RabbitMqConstants;
import com.gulewang.backend.rabbitMq.message.UserRegisterMessage;
import com.gulewang.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTopicSender {

	private static final Logger logger = LoggerFactory.getLogger(UserTopicSender.class);

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private TopicExchange topic;

	public void publishRegisterEvent(UserRegisterMessage userRegisterMessage) {
		String message = JsonUtil.toJsonString(userRegisterMessage);
		template.convertAndSend(topic.getName(), RabbitMqConstants.TOPIC_STOCK_USER_REGISTER_SUCCESS, message);
		logger.info("send message to topic: {}, message: {}", RabbitMqConstants.TOPIC_STOCK_USER_REGISTER_SUCCESS, message);
	}

}
