package com.gushui.learning.spring.reactor.service;

import com.gushui.learning.spring.reactor.domain.Notification;
import org.springframework.stereotype.Service;

/**
 * Created by Thomas on 2018/2/27.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

  @Override
  public void immediatelyNotificate(Notification notification) throws InterruptedException {
    System.out.println("Notification service started for Notification ID: " + notification.getId());
    Thread.sleep(5000);
    System.out.println("Notification service ended for Notification ID: " + notification.getId());
  }
}
