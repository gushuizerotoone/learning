package com.gushui.learning.spring.reactor.service;

import com.gushui.learning.spring.reactor.domain.Notification;

/**
 * Created by Thomas on 2018/2/27.
 */
public interface NotificationService {
  void immediatelyNotify(Notification notification) throws InterruptedException;
}
