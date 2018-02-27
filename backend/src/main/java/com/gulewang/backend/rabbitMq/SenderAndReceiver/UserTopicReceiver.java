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

import com.gulewang.backend.rabbitMq.message.UserRegisterMessage;
import com.gulewang.backend.service.CompositeUserStockService;
import com.gulewang.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserTopicReceiver {

	private static final Logger logger = LoggerFactory.getLogger(UserTopicSender.class);

	@Autowired
	CompositeUserStockService compositeUserStockService;

	@RabbitListener(queues = "#{userQueue.name}")
	public void receiveRegisterEvent(String userRegisterMessageStr) throws InterruptedException {
		logger.info("Receive register message: {}", userRegisterMessageStr);
		String userId = JsonUtil.fromJson(userRegisterMessageStr, UserRegisterMessage.class).getUserId();
		compositeUserStockService.addInterestStock(userId, Arrays.asList("SH000001", "SZ399001")); // 默认关注上证和深证指数
	}

}
